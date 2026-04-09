const express = require('express');
const db = require('../db');
const { authMiddleware } = require('../middleware/auth');

const router = express.Router();

// GET /api/dashboard/stats
router.get('/stats', authMiddleware, async (req, res) => {
  try {
    const [xpRes, filesRes, bugsRes, aiRes, achRes] = await Promise.all([
      db.query('SELECT total_xp, level FROM user_xp WHERE user_id = $1', [req.user.id]),
      db.query('SELECT COUNT(*) AS count, COALESCE(SUM(file_size),0) AS total_size FROM user_files WHERE user_id = $1', [req.user.id]),
      db.query('SELECT COUNT(*) AS total, SUM(CASE WHEN status = \'resolved\' THEN 1 ELSE 0 END) AS resolved FROM bug_reports WHERE user_id = $1', [req.user.id]),
      db.query('SELECT COUNT(*) AS count FROM ai_messages WHERE conversation_id IN (SELECT id FROM ai_conversations WHERE user_id = $1)', [req.user.id]),
      db.query('SELECT COUNT(*) AS count FROM user_achievements WHERE user_id = $1', [req.user.id])
    ]);

    const user = await db.query(
      'SELECT username, email, role, subscription_status, avatar_url, created_at FROM users WHERE id = $1',
      [req.user.id]
    );

    res.json({
      user: user.rows[0],
      xp: xpRes.rows[0] || { total_xp: 0, level: 1 },
      files: {
        count: parseInt(filesRes.rows[0].count),
        total_size: parseInt(filesRes.rows[0].total_size)
      },
      bugs: {
        total: parseInt(bugsRes.rows[0].total),
        resolved: parseInt(bugsRes.rows[0].resolved || 0)
      },
      ai: { messages: parseInt(aiRes.rows[0].count) },
      achievements: { count: parseInt(achRes.rows[0].count) }
    });
  } catch (err) {
    console.error('Dashboard stats error:', err);
    res.status(500).json({ error: 'Failed to fetch stats' });
  }
});

// GET /api/dashboard/activity  (recent activity feed)
router.get('/activity', authMiddleware, async (req, res) => {
  try {
    const xpHistory = await db.query(
      `SELECT 'xp' AS type, action AS title, description, amount AS value, created_at
       FROM xp_transactions WHERE user_id = $1 ORDER BY created_at DESC LIMIT 10`,
      [req.user.id]
    );
    const achievements = await db.query(
      `SELECT 'achievement' AS type, a.name AS title, a.description, a.xp_reward AS value, ua.earned_at AS created_at
       FROM user_achievements ua JOIN achievements a ON ua.achievement_id = a.id
       WHERE ua.user_id = $1 ORDER BY ua.earned_at DESC LIMIT 5`,
      [req.user.id]
    );

    const combined = [...xpHistory.rows, ...achievements.rows]
      .sort((a, b) => new Date(b.created_at) - new Date(a.created_at))
      .slice(0, 15);

    res.json(combined);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch activity' });
  }
});

module.exports = router;
