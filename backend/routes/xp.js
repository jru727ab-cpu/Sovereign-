const express = require('express');
const db = require('../db');
const { authMiddleware } = require('../middleware/auth');

const router = express.Router();

// XP needed per level: level = floor(sqrt(totalXp / 100)) + 1
function xpForLevel(level) {
  return Math.pow(Math.max(0, level - 1), 2) * 100;
}

// GET /api/xp/me
router.get('/me', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      'SELECT total_xp, level FROM user_xp WHERE user_id = $1',
      [req.user.id]
    );
    if (result.rows.length === 0) return res.status(404).json({ error: 'XP record not found' });

    const { total_xp, level } = result.rows[0];
    const currentLevelXp = xpForLevel(level);
    const nextLevelXp = xpForLevel(level + 1);
    const progress = nextLevelXp > currentLevelXp
      ? Math.round(((total_xp - currentLevelXp) / (nextLevelXp - currentLevelXp)) * 100)
      : 100;

    res.json({ total_xp, level, current_level_xp: currentLevelXp, next_level_xp: nextLevelXp, progress_pct: progress });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch XP' });
  }
});

// GET /api/xp/history
router.get('/history', authMiddleware, async (req, res) => {
  const limit = Math.min(50, parseInt(req.query.limit) || 20);
  try {
    const result = await db.query(
      'SELECT amount, action, description, created_at FROM xp_transactions WHERE user_id = $1 ORDER BY created_at DESC LIMIT $2',
      [req.user.id, limit]
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch XP history' });
  }
});

// GET /api/xp/leaderboard
router.get('/leaderboard', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      `SELECT u.username, u.avatar_url, x.total_xp, x.level,
              RANK() OVER (ORDER BY x.total_xp DESC) AS rank
       FROM user_xp x JOIN users u ON x.user_id = u.id
       ORDER BY x.total_xp DESC LIMIT 25`
    );

    // Find current user's rank
    const myRank = await db.query(
      `SELECT rank FROM (
         SELECT user_id, RANK() OVER (ORDER BY total_xp DESC) AS rank
         FROM user_xp
       ) ranked WHERE user_id = $1`,
      [req.user.id]
    );

    res.json({
      leaderboard: result.rows,
      my_rank: myRank.rows[0]?.rank || null
    });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch leaderboard' });
  }
});

// GET /api/xp/achievements
router.get('/achievements', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      `SELECT a.id, a.slug, a.name, a.description, a.icon, a.xp_reward,
              ua.earned_at
       FROM achievements a
       LEFT JOIN user_achievements ua ON a.id = ua.achievement_id AND ua.user_id = $1
       ORDER BY a.xp_reward DESC`,
      [req.user.id]
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch achievements' });
  }
});

module.exports = router;
