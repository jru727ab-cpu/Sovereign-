const express = require('express');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const { v4: uuidv4 } = require('uuid');
const db = require('../db');
const { authMiddleware } = require('../middleware/auth');

const router = express.Router();

// Storage limits per role (bytes)
const STORAGE_LIMITS = {
  free: 100 * 1024 * 1024,       // 100 MB
  pro: 5 * 1024 * 1024 * 1024,   // 5 GB
  enterprise: 50 * 1024 * 1024 * 1024 // 50 GB
};

const UPLOAD_DIR = process.env.UPLOAD_DIR || path.join(__dirname, '..', 'uploads');
if (!fs.existsSync(UPLOAD_DIR)) fs.mkdirSync(UPLOAD_DIR, { recursive: true });

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    const userDir = path.join(UPLOAD_DIR, req.user.id);
    if (!fs.existsSync(userDir)) fs.mkdirSync(userDir, { recursive: true });
    cb(null, userDir);
  },
  filename: (req, file, cb) => {
    const ext = path.extname(file.originalname);
    cb(null, `${uuidv4()}${ext}`);
  }
});

function fileFilter(req, file, cb) {
  const blockedExts = ['.exe', '.bat', '.sh', '.cmd', '.ps1', '.msi', '.dll'];
  const ext = path.extname(file.originalname).toLowerCase();
  if (blockedExts.includes(ext)) {
    return cb(new Error('File type not allowed'), false);
  }
  cb(null, true);
}

const upload = multer({
  storage,
  fileFilter,
  limits: { fileSize: 100 * 1024 * 1024 } // 100MB per file
});

// POST /api/storage/upload
router.post('/upload', authMiddleware, async (req, res) => {
  // Check storage quota
  const quotaCheck = await db.query(
    'SELECT COALESCE(SUM(file_size), 0) AS used FROM user_files WHERE user_id = $1',
    [req.user.id]
  );
  const used = parseInt(quotaCheck.rows[0].used);
  const limit = STORAGE_LIMITS[req.user.role] || STORAGE_LIMITS.free;

  upload.single('file')(req, res, async (err) => {
    if (err) {
      return res.status(400).json({ error: err.message || 'Upload failed' });
    }
    if (!req.file) {
      return res.status(400).json({ error: 'No file provided' });
    }
    if (used + req.file.size > limit) {
      fs.unlinkSync(req.file.path);
      return res.status(403).json({ error: 'Storage quota exceeded' });
    }

    try {
      const result = await db.query(
        `INSERT INTO user_files (user_id, filename, original_name, mime_type, file_size, storage_path)
         VALUES ($1, $2, $3, $4, $5, $6) RETURNING id, filename, original_name, mime_type, file_size, created_at`,
        [
          req.user.id,
          req.file.filename,
          req.file.originalname,
          req.file.mimetype,
          req.file.size,
          req.file.path
        ]
      );

      await awardXP(req.user.id, 3, 'file_upload', `Uploaded ${req.file.originalname}`);
      res.status(201).json(result.rows[0]);
    } catch (dbErr) {
      fs.unlinkSync(req.file.path);
      console.error('Storage DB error:', dbErr);
      res.status(500).json({ error: 'Failed to save file record' });
    }
  });
});

// GET /api/storage/files
router.get('/files', authMiddleware, async (req, res) => {
  const page = Math.max(1, parseInt(req.query.page) || 1);
  const limit = Math.min(100, parseInt(req.query.limit) || 20);
  const offset = (page - 1) * limit;

  try {
    const result = await db.query(
      `SELECT id, filename, original_name, mime_type, file_size, is_public, download_count, created_at
       FROM user_files WHERE user_id = $1 ORDER BY created_at DESC LIMIT $2 OFFSET $3`,
      [req.user.id, limit, offset]
    );
    const total = await db.query('SELECT COUNT(*), COALESCE(SUM(file_size),0) AS used FROM user_files WHERE user_id = $1', [req.user.id]);
    const role = req.user.role || 'free';

    res.json({
      files: result.rows,
      total: parseInt(total.rows[0].count),
      storage_used: parseInt(total.rows[0].used),
      storage_limit: STORAGE_LIMITS[role] || STORAGE_LIMITS.free,
      page,
      limit
    });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch files' });
  }
});

// GET /api/storage/files/:id/download
router.get('/files/:id/download', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      'SELECT * FROM user_files WHERE id = $1 AND user_id = $2',
      [req.params.id, req.user.id]
    );
    if (result.rows.length === 0) return res.status(404).json({ error: 'File not found' });

    const file = result.rows[0];
    const resolvedPath = path.resolve(file.storage_path);
    const resolvedUploadDir = path.resolve(UPLOAD_DIR);
    if (!resolvedPath.startsWith(resolvedUploadDir + path.sep)) {
      return res.status(403).json({ error: 'Access denied' });
    }
    if (!fs.existsSync(resolvedPath)) {
      return res.status(404).json({ error: 'File no longer exists on disk' });
    }

    await db.query('UPDATE user_files SET download_count = download_count + 1 WHERE id = $1', [file.id]);
    res.setHeader('Content-Disposition', `attachment; filename="${file.original_name}"`);
    res.setHeader('Content-Type', file.mime_type || 'application/octet-stream');
    res.sendFile(resolvedPath);
  } catch (err) {
    res.status(500).json({ error: 'Download failed' });
  }
});

// DELETE /api/storage/files/:id
router.delete('/files/:id', authMiddleware, async (req, res) => {
  try {
    const result = await db.query(
      'DELETE FROM user_files WHERE id = $1 AND user_id = $2 RETURNING storage_path',
      [req.params.id, req.user.id]
    );
    if (result.rows.length === 0) return res.status(404).json({ error: 'File not found' });

    const filePath = result.rows[0].storage_path;
    const resolvedPath = path.resolve(filePath);
    const resolvedUploadDir = path.resolve(UPLOAD_DIR);
    if (resolvedPath.startsWith(resolvedUploadDir + path.sep) && fs.existsSync(resolvedPath)) {
      fs.unlinkSync(resolvedPath);
    }
    res.json({ message: 'File deleted' });
  } catch (err) {
    res.status(500).json({ error: 'Delete failed' });
  }
});

async function awardXP(userId, amount, action, description) {
  const client = await require('../db').pool.connect();
  try {
    await client.query('BEGIN');
    await client.query(
      'INSERT INTO xp_transactions (user_id, amount, action, description) VALUES ($1, $2, $3, $4)',
      [userId, amount, action, description]
    );
    await client.query(
      'UPDATE user_xp SET total_xp = total_xp + $1, level = GREATEST(1, FLOOR(SQRT((total_xp + $1) / 100.0))::int + 1) WHERE user_id = $2',
      [amount, userId]
    );
    await client.query('COMMIT');
  } catch (err) {
    await client.query('ROLLBACK');
    console.error('XP award error:', err);
  } finally {
    client.release();
  }
}

module.exports = router;
