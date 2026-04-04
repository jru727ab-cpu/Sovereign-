# Cost Controls — Running CIVILTAS at Zero / Low Ongoing Cost

This document describes how to run the full CIVILTAS Android application with
**zero or near-zero ongoing infrastructure cost**, which services are optional,
and which free-tier alternatives are available for each.

---

## Guiding Principles

1. **Offline-first.** The game must be fully playable with no network access.  
2. **Pay only for what you use.** Every optional service is gated by a feature flag and disabled by default.  
3. **Prefer free tiers.** All recommended third-party services have generous free tiers that cover typical indie game traffic.  
4. **No background polling.** The app never makes periodic network calls on its own; sync is triggered explicitly by the user or a deliberate operator flag.

---

## Feature Flag Reference

Edit `feature_flags.json` (repo root, or `app/src/main/assets/feature_flags.json` once the Android project scaffold is added) to control which services are active.

```json
{
  "online_sync_enabled": false,
  "crash_reporting_enabled": false,
  "analytics_enabled": false,
  "ads_enabled": false,
  "payments_enabled": false,
  "cloud_backup_enabled": false
}
```

**All flags default to `false`.** The app works completely without enabling any flag.

---

## Service-by-Service Breakdown

### 1. Data Storage — always free

| Layer | Technology | Cost |
|---|---|---|
| Primary game state | Room (SQLite) | **Free** — on-device only |
| Lightweight settings | SharedPreferences | **Free** — on-device only |
| Idle progress | Deterministic on-device calculation | **Free** — no server needed |

**No action required.** This always runs locally for free.

---

### 2. Crash Reporting (`crash_reporting_enabled`)

**Default:** `false` — crashes are logged locally only (internal storage, never uploaded).

**When to enable:** Once you have active users and need actionable crash data.

| Option | Free Tier | Notes |
|---|---|---|
| Firebase Crashlytics | **Free** (unlimited crashes) | Best for Android; no cost at any scale |
| Sentry | 5,000 errors/month free | Self-hostable for zero cost |
| Local only (default) | **Free** | Crash log written to internal storage; user can share via "Report a Problem" screen |

**Recommendation:** Start with local-only. Enable Crashlytics (free) once you hit 100+ users.

**Setup:**
```json
{ "crash_reporting_enabled": true }
```
Then add `google-services.json` from Firebase Console (free project).

---

### 3. Analytics (`analytics_enabled`)

**Default:** `false` — no user data is collected or sent anywhere.

**When to enable:** When you need retention metrics or funnel analysis.

| Option | Free Tier | Notes |
|---|---|---|
| Firebase Analytics | **Free** (unlimited events) | Privacy-compliant; no cost |
| Mixpanel | 20M events/month free | More detailed funnels |
| None (default) | **Free** | Recommended for MVP |

**Recommendation:** Keep disabled for MVP. Enable Firebase Analytics (free) once you need retention data.

---

### 4. Cloud Sync / Backup (`online_sync_enabled`, `cloud_backup_enabled`)

**Default:** `false` — game state is stored locally only.

**When to enable:** When players ask for cross-device progress or backup-restore.

| Option | Free Tier | Notes |
|---|---|---|
| Firebase Firestore | 1 GB storage, 50k reads/day free | Sufficient for ~10,000 active users |
| Firebase Realtime DB | 1 GB storage, 10 GB/month free | Simpler for flat game state |
| Room export/import | **Free** | Local file export; user manually backs up |
| DeNet (decentralized) | Varies by provider | Enable only if decentralized storage is a product feature |

**Recommendation:** Start with local export/import (free). Add Firestore free tier when cross-device is requested.

**Cost estimate at scale:**
- 1,000 daily active users × 1 sync/day × ~5 KB = ~5 MB writes/day → well within Firestore free tier.
- Firestore paid tier only kicks in above 1 GB stored or 50,000 reads/day — you will not hit this in MVP.

---

### 5. Ads (`ads_enabled`)

**Default:** `false` — no ads shown.

**When to enable:** When you want optional rewarded ads as a monetization layer.

**Implementation rule: rewarded ads only.** No forced interstitials, no banner ads.

| Network | Revenue share | Notes |
|---|---|---|
| Google AdMob | ~50–70% to developer | Free to integrate; no upfront cost |
| Unity Ads | ~50–60% to developer | Good for gaming inventory |
| IronSource | ~50–65% to developer | Good eCPM for idle games |

**Cost:** $0 upfront. Revenue is shared from what players generate.

**How to enable:**
```json
{ "ads_enabled": true }
```
Then configure your AdMob App ID in `AndroidManifest.xml`.

---

### 6. Payments (`payments_enabled`)

**Default:** `false` — IAP screens are hidden; no billing SDK is initialised.

**When to enable:** When you are ready to monetize with real money.

| Distribution | Payment method | Notes |
|---|---|---|
| Google Play Store | Google Play Billing (required) | 15% fee on first $1M/year, 30% after |
| Direct APK / F-Droid | Stripe (cards) + WalletConnect (crypto) | 2.9% + $0.30 per card transaction |

**Cost:** No upfront cost; fees are taken per transaction only.

---

## CI / CD — Free Pipeline

The repository uses **GitHub CodeQL** for security scanning (replaces the previous paid Fortify workflow).

| Workflow | Tool | Trigger | Cost |
|---|---|---|---|
| Security scan | GitHub CodeQL | Push to main, weekly | **Free** for public repos |
| Unit tests | `./gradlew test` | Push / PR | **Free** (GitHub Actions free tier) |

GitHub Actions free tier includes **2,000 minutes/month** on `ubuntu-latest` runners — more than enough for this project.

---

## Monthly Cost Estimate (MVP, ~500 users)

| Service | Monthly cost |
|---|---|
| Room DB (local) | $0 |
| Crash reporting (Crashlytics free) | $0 |
| Analytics (Firebase free) | $0 |
| Cloud sync (Firestore free tier) | $0 |
| CI (GitHub Actions free tier) | $0 |
| **Total** | **$0** |

You only start paying when you exceed free tier limits, which requires significant scale (10,000+ daily active users for most services).

---

## Avoiding Accidental Spend

1. **Do not enable `online_sync_enabled: true` in the default `feature_flags.json` checked into the repo.** If you need it for a release build, use a build-variant override or CI secret injection.
2. **Set Firebase budget alerts** at $1, $5, and $10/month so you get notified before any bill appears.
3. **Never run scheduled network sync in the background.** All sync must be user-triggered or done at app foreground transitions only.
4. **Do not add paid CI services** (e.g. Fortify, SonarCloud paid tier, Bitrise paid) to the main pipeline. Use GitHub CodeQL (free) and Android Lint (free) instead.
5. **Review third-party SDK initialization.** Some SDKs (e.g., older Firebase Performance) auto-initialize and make network calls on every app start. Always call `FirebaseApp.initializeApp(context)` only when the relevant flag is `true`.
