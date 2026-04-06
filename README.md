# ⚡ Alday.work — Full-Stack Dev Platform

A production-ready full-stack web application with:
- 🤖 **Personal AI Agent** (GPT-4o-mini, optimized)
- 🐛 **Bug Finder / Crusher** (AI-powered code analysis)
- 📁 **Secure File Storage** (per-user, quota-based)
- 🏆 **XP / Gamification** (levels, achievements, leaderboard)
- 💳 **Stripe Monetization** (Free / Pro / Enterprise)
- 🔒 **JWT Authentication** (signup, login, role-based access)
- 📊 **User Dashboard** (stats, activity feed, settings)

---

## Architecture

```
/                          ← GitHub Pages (static frontend)
├── index.html             ← Landing page + auth modal
├── dashboard.html         ← App dashboard (SPA)
├── styles.css             ← All styles
├── script.js              ← Landing extras
└── js/
    ├── config.js          ← API_BASE config
    ├── auth.js            ← JWT auth helpers + login/signup UI
    ├── ai-agent.js        ← AI chat interface
    ├── bug-finder.js      ← Bug detection UI
    ├── storage.js         ← File upload/download UI
    ├── xp.js              ← XP, leaderboard, achievements
    ├── payments.js        ← Stripe billing UI
    └── dashboard.js       ← Main SPA router + stats

backend/                   ← Node.js + Express (deploy separately)
├── server.js              ← Express app entry point
├── package.json
├── .env.example           ← Copy to .env and configure
├── routes/
│   ├── auth.js            ← /api/auth/*
│   ├── ai.js              ← /api/ai/*
│   ├── bugs.js            ← /api/bugs/*
│   ├── storage.js         ← /api/storage/*
│   ├── xp.js              ← /api/xp/*
│   ├── payments.js        ← /api/payments/*
│   └── dashboard.js       ← /api/dashboard/*
├── middleware/
│   └── auth.js            ← JWT verification
└── db/
    ├── index.js           ← pg Pool
    ├── schema.sql         ← Full database schema
    └── migrate.js         ← Run migrations
```

---

## Quick Start

### Prerequisites
- Node.js 18+
- PostgreSQL 14+
- OpenAI API key
- Stripe account (optional)

### Backend Setup

```bash
cd backend
cp .env.example .env
# Fill in DATABASE_URL, JWT_SECRET, OPENAI_API_KEY, STRIPE_* keys

npm install
npm run migrate     # Run database migrations
npm start           # Start on port 3000
```

### Frontend Setup

The frontend is static HTML/CSS/JS — just host on GitHub Pages.

Update `js/config.js` with your backend URL:
```js
window.API_BASE = 'https://your-backend.example.com';
```

---

## API Reference

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/signup` | — | Create account |
| POST | `/api/auth/login` | — | Login, get JWT |
| GET | `/api/auth/me` | ✓ | Get current user |
| PUT | `/api/auth/profile` | ✓ | Update profile |
| POST | `/api/ai/chat` | ✓ | Chat with AI agent |
| GET | `/api/ai/conversations` | ✓ | List conversations |
| POST | `/api/bugs/analyze` | ✓ | Analyze code for bugs |
| GET | `/api/bugs/reports` | ✓ | List bug reports |
| POST | `/api/storage/upload` | ✓ | Upload file |
| GET | `/api/storage/files` | ✓ | List files |
| GET | `/api/xp/me` | ✓ | Get XP/level |
| GET | `/api/xp/leaderboard` | ✓ | Global leaderboard |
| GET | `/api/xp/achievements` | ✓ | Achievements |
| GET | `/api/payments/plans` | — | List plans |
| POST | `/api/payments/create-checkout` | ✓ | Stripe checkout |
| POST | `/api/payments/webhook` | — | Stripe webhook |
| GET | `/api/dashboard/stats` | ✓ | Dashboard stats |

---

## Subscription Plans

| Feature | Free | Pro ($19/mo) | Enterprise ($99/mo) |
|---------|------|--------------|---------------------|
| AI messages/day | 20 | 500 | Unlimited |
| Storage | 100MB | 5GB | 50GB |
| Bug analysis | 5/day | Unlimited | Unlimited |
| XP Boost | 1x | 2x | 5x |
| Support | — | Priority | Dedicated |

---

## Deployment

### Backend (e.g. Railway, Render, Fly.io)
```bash
# Set environment variables in your host's dashboard
npm start
```

### Stripe Webhooks
Point `https://your-backend.com/api/payments/webhook` to your Stripe webhook endpoint.

### Frontend (GitHub Pages)
Push to your `main` branch — GitHub Pages auto-deploys.

---

## Design Principles
- **No credit waste** — GPT-4o-mini with 10-message context window, 1024 token limit
- **No unnecessary deps** — only essentials (express, pg, bcryptjs, jsonwebtoken, openai, stripe, multer, helmet, cors, uuid)
- **Efficient DB queries** — indexed, paginated, uses `COALESCE`, `ON CONFLICT DO NOTHING`
- **Frontend caching** — localStorage for JWT + user data
- **Lazy loading** — each dashboard view initializes only when first visited
