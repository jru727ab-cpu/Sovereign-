const request = require('supertest');
const app = require('../server');

// Mock database
jest.mock('../db', () => ({
  query: jest.fn()
}));

const db = require('../db');

describe('Health check', () => {
  it('GET /health returns ok', async () => {
    const res = await request(app).get('/health');
    expect(res.status).toBe(200);
    expect(res.body.status).toBe('ok');
  });
});

describe('Auth routes', () => {
  beforeEach(() => jest.clearAllMocks());

  it('POST /api/auth/signup - missing fields returns 400', async () => {
    const res = await request(app).post('/api/auth/signup').send({ email: 'test@test.com' });
    expect(res.status).toBe(400);
  });

  it('POST /api/auth/signup - short password returns 400', async () => {
    const res = await request(app).post('/api/auth/signup').send({ email: 'test@test.com', password: 'short', username: 'testuser' });
    expect(res.status).toBe(400);
  });

  it('POST /api/auth/signup - invalid username returns 400', async () => {
    const res = await request(app).post('/api/auth/signup').send({ email: 'test@test.com', password: 'password123', username: 'a' });
    expect(res.status).toBe(400);
  });

  it('POST /api/auth/login - missing credentials returns 400', async () => {
    const res = await request(app).post('/api/auth/login').send({});
    expect(res.status).toBe(400);
  });

  it('GET /api/auth/me - no token returns 401', async () => {
    const res = await request(app).get('/api/auth/me');
    expect(res.status).toBe(401);
  });
});

describe('Bug routes', () => {
  it('POST /api/bugs/analyze - no token returns 401', async () => {
    const res = await request(app).post('/api/bugs/analyze').send({ code: 'const x = 1' });
    expect(res.status).toBe(401);
  });

  it('GET /api/bugs/reports - no token returns 401', async () => {
    const res = await request(app).get('/api/bugs/reports');
    expect(res.status).toBe(401);
  });
});

describe('Storage routes', () => {
  it('POST /api/storage/upload - no token returns 401', async () => {
    const res = await request(app).post('/api/storage/upload');
    expect(res.status).toBe(401);
  });
});

describe('XP routes', () => {
  it('GET /api/xp/me - no token returns 401', async () => {
    const res = await request(app).get('/api/xp/me');
    expect(res.status).toBe(401);
  });

  it('GET /api/xp/leaderboard - no token returns 401', async () => {
    const res = await request(app).get('/api/xp/leaderboard');
    expect(res.status).toBe(401);
  });
});

describe('Payment routes', () => {
  it('GET /api/payments/plans - public endpoint', async () => {
    const res = await request(app).get('/api/payments/plans');
    expect(res.status).toBe(200);
    expect(res.body).toHaveProperty('free');
    expect(res.body).toHaveProperty('pro');
    expect(res.body).toHaveProperty('enterprise');
  });

  it('GET /api/payments/subscription - no token returns 401', async () => {
    const res = await request(app).get('/api/payments/subscription');
    expect(res.status).toBe(401);
  });
});

describe('404 handler', () => {
  it('unknown route returns 404', async () => {
    const res = await request(app).get('/api/nonexistent');
    expect(res.status).toBe(404);
  });
});
