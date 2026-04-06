// payments.js — Billing/subscription UI
const Payments = (() => {
  const PLAN_ICONS = { free: '🆓', pro: '💎', enterprise: '🏢' };

  async function loadCurrentSubscription() {
    const panel = document.getElementById('currentPlan');
    if (!panel) return;
    try {
      const res = await Auth.apiFetch('/api/payments/subscription');
      const { plan, status, plan_details } = await res.json();
      panel.innerHTML = `
        <span style="font-size:2rem">${PLAN_ICONS[plan] || '🔧'}</span>
        <div>
          <div style="font-size:1.1rem;font-weight:700">${plan_details?.name || plan} Plan</div>
          <div style="font-size:.9rem;color:var(--text-muted)">
            Status: <span style="color:${status === 'active' ? 'var(--success)' : 'var(--warning)'}">${status}</span>
          </div>
        </div>`;
    } catch { panel.innerHTML = '<div class="loading">Failed to load subscription</div>'; }
  }

  async function loadPlans() {
    const grid = document.getElementById('billingPlans');
    if (!grid) return;
    try {
      const res = await fetch(`${window.API_BASE}/api/payments/plans`);
      const plans = await res.json();
      grid.innerHTML = Object.entries(plans).map(([key, plan]) => `
        <div class="card plan-card ${key === 'pro' ? 'featured' : ''}">
          ${key === 'pro' ? '<div class="plan-badge">Most Popular</div>' : ''}
          <div class="plan-name">${PLAN_ICONS[key] || ''} ${plan.name}</div>
          <div class="plan-price">$${(plan.price / 100).toFixed(0)}<span>/mo</span></div>
          <ul class="plan-features">
            ${(plan.features || []).map(f => `<li>${escapeHtml(f)}</li>`).join('')}
          </ul>
          ${key === 'free'
            ? `<button class="btn btn-outline" disabled>Current Free Plan</button>`
            : `<button class="btn ${key === 'pro' ? 'btn-primary' : 'btn-outline'}" data-plan="${key}">
                 Upgrade to ${plan.name}
               </button>`}
        </div>`).join('');

      grid.querySelectorAll('[data-plan]').forEach(btn => {
        btn.addEventListener('click', () => startCheckout(btn.dataset.plan, btn));
      });
    } catch { grid.innerHTML = '<div class="loading">Failed to load plans</div>'; }
  }

  async function startCheckout(plan, btn) {
    const planName = PLANS[plan]?.name || plan;
    btn.disabled = true; btn.textContent = 'Redirecting...';
    try {
      const res = await Auth.apiFetch('/api/payments/create-checkout', {
        method: 'POST', body: JSON.stringify({ plan })
      });
      const data = await res.json();
      if (!res.ok) { alert(data.error || 'Failed to start checkout'); return; }
      window.location.href = data.checkout_url;
    } catch { alert('Network error'); }
    finally { btn.disabled = false; btn.textContent = `Upgrade to ${planName}`; }
  }

  function escapeHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  }

  function init() {
    loadCurrentSubscription();
    loadPlans();

    const cancelBtn = document.getElementById('cancelSubBtn');
    if (cancelBtn) {
      cancelBtn.addEventListener('click', async () => {
        if (!confirm('Cancel your subscription? You will lose access at the end of the billing period.')) return;
        cancelBtn.disabled = true;
        try {
          const res = await Auth.apiFetch('/api/payments/cancel', { method: 'POST' });
          const data = await res.json();
          if (res.ok) { alert('Subscription cancelled.'); loadCurrentSubscription(); }
          else alert(data.error || 'Failed to cancel');
        } catch { alert('Network error'); }
        finally { cancelBtn.disabled = false; }
      });
    }

    // Handle post-checkout redirect
    const params = new URLSearchParams(window.location.search);
    if (params.get('payment') === 'success') {
      alert('🎉 Payment successful! Your plan has been upgraded.');
      window.history.replaceState({}, '', '/dashboard.html');
    }
  }

  return { init };
})();
