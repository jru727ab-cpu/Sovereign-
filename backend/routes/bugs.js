const express = require('express');
const OpenAI = require('openai');
const db = require('../db');
const { authMiddleware } = require('../middleware/auth');

const router = express.Router();
const openai = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });

const BUG_SYSTEM_PROMPT = `You are an expert code reviewer and bug detector.
Analyze the provided code and return ONLY a valid JSON object (no markdown, no extra text) with this structure:
{
  "bugs": [
    {
      "line": <number or null>,
      "type": "<syntax|logic|security|performance|style>",
      "severity": "<low|medium|high|critical>",
      "description": "<brief description>",
      "fix": "<suggested fix code or explanation>"
    }
  ],
  "overall_severity": "<low|medium|high|critical>",
  "summary": "<1-2 sentence summary>"
}
If no bugs found, return {"bugs":[],"overall_severity":"low","summary":"No bugs detected."}.`;

// POST /api/bugs/analyze
router.post('/analyze', authMiddleware, async (req, res) => {
  const { code, language, title } = req.body;
  if (!code || !code.trim()) {
    return res.status(400).json({ error: 'code is required' });
  }
  if (code.length > 10000) {
    return res.status(400).json({ error: 'Code too long (max 10000 chars)' });
  }

  try {
    const prompt = `Language: ${language || 'unknown'}\n\nCode:\n\`\`\`\n${code}\n\`\`\``;

    const completion = await openai.chat.completions.create({
      model: 'gpt-4o-mini',
      messages: [
        { role: 'system', content: BUG_SYSTEM_PROMPT },
        { role: 'user', content: prompt }
      ],
      max_tokens: 1024,
      temperature: 0.1,
      response_format: { type: 'json_object' }
    });

    let analysis;
    try {
      analysis = JSON.parse(completion.choices[0].message.content);
    } catch {
      analysis = { bugs: [], overall_severity: 'low', summary: 'Analysis parsing failed' };
    }

    const result = await db.query(
      `INSERT INTO bug_reports (user_id, title, code_snippet, language, bugs_found, fixes_suggested, severity)
       VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING id, created_at`,
      [
        req.user.id,
        title || 'Code Analysis',
        code,
        language || 'unknown',
        JSON.stringify(analysis.bugs || []),
        JSON.stringify((analysis.bugs || []).map(b => b.fix).filter(Boolean)),
        analysis.overall_severity || 'low'
      ]
    );

    // Award XP for bug hunting
    if ((analysis.bugs || []).length > 0) {
      await awardXP(req.user.id, 5 * analysis.bugs.length, 'bug_found', `Found ${analysis.bugs.length} bug(s)`);
    }

    res.json({
      id: result.rows[0].id,
      bugs: analysis.bugs || [],
      overall_severity: analysis.overall_severity || 'low',
      summary: analysis.summary || '',
      created_at: result.rows[0].created_at
    });
  } catch (err) {
    console.error('Bug analysis error:', err);
    res.status(500).json({ error: 'Bug analysis failed' });
  }
});

// GET /api/bugs/reports
router.get('/reports', authMiddleware, async (req, res) => {
  const page = Math.max(1, parseInt(req.query.page) || 1);
  const limit = Math.min(50, parseInt(req.query.limit) || 20);
  const offset = (page - 1) * limit;

  try {
    const result = await db.query(
      `SELECT id, title, language, severity, status, created_at,
              jsonb_array_length(bugs_found) AS bug_count
       FROM bug_reports WHERE user_id = $1
       ORDER BY created_at DESC LIMIT $2 OFFSET $3`,
      [req.user.id, limit, offset]
    );
    const total = await db.query('SELECT COUNT(*) FROM bug_reports WHERE user_id = $1', [req.user.id]);
    res.json({ reports: result.rows, total: parseInt(total.rows[0].count), page, limit });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch reports' });
  }
});

// GET /api/bugs/reports/:id
router.get('/reports/:id', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      'SELECT * FROM bug_reports WHERE id = $1 AND user_id = $2',
      [req.params.id, req.user.id]
    );
    if (result.rows.length === 0) return res.status(404).json({ error: 'Report not found' });
    res.json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch report' });
  }
});

// PUT /api/bugs/reports/:id/status
router.put('/reports/:id/status', authMiddleware, async (req, res) => {
  const { status } = req.body;
  if (!['open', 'resolved', 'wont_fix'].includes(status)) {
    return res.status(400).json({ error: 'Invalid status' });
  }
  try {
    const result = await db.query(
      'UPDATE bug_reports SET status = $1 WHERE id = $2 AND user_id = $3 RETURNING id, status',
      [status, req.params.id, req.user.id]
    );
    if (result.rows.length === 0) return res.status(404).json({ error: 'Report not found' });
    res.json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ error: 'Failed to update status' });
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
