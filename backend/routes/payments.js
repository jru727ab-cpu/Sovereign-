const express = require('express');
const db = require('../db');
const { authMiddleware } = require('../middleware/auth');

const router = express.Router();

let stripe;
try {
  stripe = require('stripe')(process.env.STRIPE_SECRET_KEY);
} catch {
  console.warn('Stripe not configured');
}

const PLANS = {
  free: { name: 'Free', price: 0, ai_messages_per_day: 20, storage_mb: 100, features: ['AI Agent (20/day)', '100MB Storage', 'Bug Finder (5/day)'] },
  pro: { name: 'Pro', price: 1900, ai_messages_per_day: 500, storage_mb: 5120, features: ['AI Agent (500/day)', '5GB Storage', 'Unlimited Bug Finder', 'Priority Support', 'XP Boost x2'], stripe_price_id: process.env.STRIPE_PRO_PRICE_ID },
  enterprise: { name: 'Enterprise', price: 9900, ai_messages_per_day: -1, storage_mb: 51200, features: ['Unlimited AI Agent', '50GB Storage', 'Unlimited Bug Finder', 'Dedicated Support', 'XP Boost x5', 'Custom Integrations'], stripe_price_id: process.env.STRIPE_ENTERPRISE_PRICE_ID }
};

// GET /api/payments/plans
router.get('/plans', (req, res) => {
  res.json(PLANS);
});

// POST /api/payments/create-checkout
router.post('/create-checkout', authMiddleware, async (req, res) => {
  const { plan } = req.body;
  if (!stripe) return res.status(503).json({ error: 'Payment system not configured' });
  if (!['pro', 'enterprise'].includes(plan)) {
    return res.status(400).json({ error: 'Invalid plan' });
  }

  const planData = PLANS[plan];
  if (!planData.stripe_price_id) {
    return res.status(503).json({ error: 'Stripe price ID not configured for this plan' });
  }

  try {
    const user = await db.query('SELECT email, stripe_customer_id FROM users WHERE id = $1', [req.user.id]);
    if (user.rows.length === 0) return res.status(404).json({ error: 'User not found' });

    let customerId = user.rows[0].stripe_customer_id;
    if (!customerId) {
      const customer = await stripe.customers.create({ email: user.rows[0].email, metadata: { user_id: req.user.id } });
      customerId = customer.id;
      await db.query('UPDATE users SET stripe_customer_id = $1 WHERE id = $2', [customerId, req.user.id]);
    }

    const session = await stripe.checkout.sessions.create({
      customer: customerId,
      mode: 'subscription',
      payment_method_types: ['card'],
      line_items: [{ price: planData.stripe_price_id, quantity: 1 }],
      success_url: `${process.env.FRONTEND_URL}/dashboard.html?payment=success`,
      cancel_url: `${process.env.FRONTEND_URL}/dashboard.html?payment=cancelled`,
      metadata: { user_id: req.user.id, plan }
    });

    res.json({ checkout_url: session.url });
  } catch (err) {
    console.error('Checkout error:', err);
    res.status(500).json({ error: 'Failed to create checkout session' });
  }
});

// POST /api/payments/webhook  (Stripe webhook endpoint)
router.post('/webhook', express.raw({ type: 'application/json' }), async (req, res) => {
  if (!stripe) return res.status(503).end();

  const sig = req.headers['stripe-signature'];
  let event;
  try {
    event = stripe.webhooks.constructEvent(req.body, sig, process.env.STRIPE_WEBHOOK_SECRET);
  } catch (err) {
    return res.status(400).json({ error: `Webhook Error: ${err.message}` });
  }

  try {
    switch (event.type) {
      case 'checkout.session.completed': {
        const session = event.data.object;
        const userId = session.metadata?.user_id;
        const plan = session.metadata?.plan;
        if (userId && plan) {
          await db.query(
            'UPDATE users SET role = $1, subscription_status = $2, stripe_subscription_id = $3 WHERE id = $4',
            [plan, 'active', session.subscription, userId]
          );
          // Award XP for subscribing
          if (plan === 'pro' || plan === 'enterprise') {
            const ach = await db.query("SELECT id, xp_reward FROM achievements WHERE slug = 'pro_subscriber'");
            if (ach.rows.length > 0) {
              await db.query(
                'INSERT INTO user_achievements (user_id, achievement_id) VALUES ($1, $2) ON CONFLICT DO NOTHING',
                [userId, ach.rows[0].id]
              );
              await db.query(
                'UPDATE user_xp SET total_xp = total_xp + $1 WHERE user_id = $2',
                [ach.rows[0].xp_reward, userId]
              );
            }
          }
        }
        break;
      }
      case 'customer.subscription.deleted':
      case 'customer.subscription.paused': {
        const sub = event.data.object;
        await db.query(
          "UPDATE users SET role = 'free', subscription_status = 'cancelled' WHERE stripe_subscription_id = $1",
          [sub.id]
        );
        break;
      }
      case 'invoice.payment_failed': {
        const invoice = event.data.object;
        await db.query(
          "UPDATE users SET subscription_status = 'past_due' WHERE stripe_subscription_id = $1",
          [invoice.subscription]
        );
        break;
      }
    }
    res.json({ received: true });
  } catch (err) {
    console.error('Webhook processing error:', err);
    res.status(500).end();
  }
});

// GET /api/payments/subscription
router.get('/subscription', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      'SELECT role, subscription_status, stripe_subscription_id FROM users WHERE id = $1',
      [req.user.id]
    );
    if (result.rows.length === 0) return res.status(404).json({ error: 'User not found' });
    const { role, subscription_status, stripe_subscription_id } = result.rows[0];
    res.json({ plan: role, status: subscription_status, plan_details: PLANS[role] || PLANS.free });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch subscription' });
  }
});

// POST /api/payments/cancel
router.post('/cancel', authMiddleware, async (req, res) => {
  if (!stripe) return res.status(503).json({ error: 'Payment system not configured' });
  try {
    const result = await db.query('SELECT stripe_subscription_id FROM users WHERE id = $1', [req.user.id]);
    const subId = result.rows[0]?.stripe_subscription_id;
    if (!subId) return res.status(404).json({ error: 'No active subscription' });

    await stripe.subscriptions.cancel(subId);
    await db.query("UPDATE users SET role = 'free', subscription_status = 'cancelled' WHERE id = $1", [req.user.id]);
    res.json({ message: 'Subscription cancelled' });
  } catch (err) {
    console.error('Cancel error:', err);
    res.status(500).json({ error: 'Failed to cancel subscription' });
  }
});

module.exports = router;
