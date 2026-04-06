const express = require('express');
const OpenAI = require('openai');
const db = require('../db');
const { authMiddleware } = require('../middleware/auth');

const router = express.Router();
const openai = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });

const SYSTEM_PROMPT = `You are a highly skilled software development assistant for Alday.work.
You help users with coding questions, debugging, architecture decisions, and learning.
Be concise, accurate, and practical. Avoid unnecessary verbosity to minimize token usage.`;

// POST /api/ai/chat
router.post('/chat', authMiddleware, async (req, res) => {
  const { message, conversation_id } = req.body;
  if (!message || !message.trim()) {
    return res.status(400).json({ error: 'Message is required' });
  }

  try {
    let convId = conversation_id;

    // Create new conversation if none provided
    if (!convId) {
      const conv = await db.query(
        'INSERT INTO ai_conversations (user_id, title) VALUES ($1, $2) RETURNING id',
        [req.user.id, message.slice(0, 60)]
      );
      convId = conv.rows[0].id;
    } else {
      // Verify conversation belongs to user
      const check = await db.query(
        'SELECT id FROM ai_conversations WHERE id = $1 AND user_id = $2',
        [convId, req.user.id]
      );
      if (check.rows.length === 0) return res.status(404).json({ error: 'Conversation not found' });
    }

    // Fetch last 10 messages for context (keeps token usage low)
    const history = await db.query(
      `SELECT role, content FROM ai_messages
       WHERE conversation_id = $1 ORDER BY created_at DESC LIMIT 10`,
      [convId]
    );
    const messages = [
      { role: 'system', content: SYSTEM_PROMPT },
      ...history.rows.reverse(),
      { role: 'user', content: message }
    ];

    // Save user message
    await db.query(
      'INSERT INTO ai_messages (conversation_id, role, content) VALUES ($1, $2, $3)',
      [convId, 'user', message]
    );

    // Call OpenAI with efficient model
    const completion = await openai.chat.completions.create({
      model: 'gpt-4o-mini',
      messages,
      max_tokens: 1024,
      temperature: 0.7
    });

    const reply = completion.choices[0].message.content;
    const tokensUsed = completion.usage?.total_tokens || 0;

    // Save assistant reply
    await db.query(
      'INSERT INTO ai_messages (conversation_id, role, content, tokens_used) VALUES ($1, $2, $3, $4)',
      [convId, 'assistant', reply, tokensUsed]
    );

    // Award XP for AI usage
    await awardXP(req.user.id, 2, 'ai_message', 'Sent AI message');

    res.json({ reply, conversation_id: convId, tokens_used: tokensUsed });
  } catch (err) {
    console.error('AI chat error:', err);
    res.status(500).json({ error: 'AI request failed' });
  }
});

// GET /api/ai/conversations
router.get('/conversations', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      `SELECT id, title, model, created_at, updated_at FROM ai_conversations
       WHERE user_id = $1 ORDER BY updated_at DESC LIMIT 50`,
      [req.user.id]
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch conversations' });
  }
});

// GET /api/ai/conversations/:id/messages
router.get('/conversations/:id/messages', authMiddleware, async (req, res) => {
  try {
    const check = await db.query(
      'SELECT id FROM ai_conversations WHERE id = $1 AND user_id = $2',
      [req.params.id, req.user.id]
    );
    if (check.rows.length === 0) return res.status(404).json({ error: 'Conversation not found' });

    const result = await db.query(
      "SELECT id, role, content, tokens_used, created_at FROM ai_messages WHERE conversation_id = $1 AND role != 'system' ORDER BY created_at ASC",
      [req.params.id]
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch messages' });
  }
});

// DELETE /api/ai/conversations/:id
router.delete('/conversations/:id', authMiddleware, async (req, res) => {
  try {
    await db.query(
      'DELETE FROM ai_conversations WHERE id = $1 AND user_id = $2',
      [req.params.id, req.user.id]
    );
    res.json({ message: 'Conversation deleted' });
  } catch (err) {
    res.status(500).json({ error: 'Failed to delete conversation' });
  }
});

async function awardXP(userId, amount, action, description) {
  try {
    await db.query(
      'INSERT INTO xp_transactions (user_id, amount, action, description) VALUES ($1, $2, $3, $4)',
      [userId, amount, action, description]
    );
    await db.query(
      'UPDATE user_xp SET total_xp = total_xp + $1, level = GREATEST(1, FLOOR(SQRT((total_xp + $1) / 100.0))::int + 1) WHERE user_id = $2',
      [amount, userId]
    );
  } catch (err) {
    console.error('XP award error:', err);
  }
}

module.exports = router;
