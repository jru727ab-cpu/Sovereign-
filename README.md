# CIVILTAS — Android Idle Mining & Civilization Game

> *Mine resources, rebuild civilization, uncover the hidden Gnosis — before the next catastrophe strikes.*

---

## Table of Contents
- [About](#about)
- [Core Loop](#core-loop)
- [Getting Started](#getting-started)
- [Architecture](#architecture)
- [Feature Flags](#feature-flags)
- [Cost Controls](#cost-controls)
- [Monetization](#monetization)
- [Contributing](#contributing)

---

## About

CIVILTAS is an Android idle/incremental game where players:

1. **Mine earth resources** — metals, gold, oil, rare minerals — through passive idle collection.
2. **Rebuild civilization** — construct buildings, research technologies, and grow your settlement after a catastrophic collapse.
3. **Discover hidden Gnosis** — uncover esoteric knowledge held by a secret society that knows the next catastrophe is coming.
4. **Survive or hoard** — decide whether to build for now or stockpile for the next world-ending event.

The game is designed to be **fully playable offline**. Online/cloud features are optional add-ons, never a requirement to enjoy the core experience.

---

## Core Loop

```
Wake up → Collect idle resources → Spend on buildings/research
       → Uncover knowledge clues → React to Catastrophe Forecast meter
       → Sleep → (offline progress continues)
```

**XP / Progression sources:**
- Mining runs (idle + active)
- Building construction & upgrades
- Research completions
- Expedition outcomes
- Daily goals & streaks
- Secret Society rank advancement

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 26+
- Java 17 / Kotlin 1.9+

### Build

```bash
# Debug APK (no signing key needed)
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

The debug APK runs fully offline with no external accounts required.

---

## Architecture

CIVILTAS follows an **offline-first** architecture:

```
UI (Jetpack Compose)
       │
ViewModel (StateFlow)
       │
Repository
 ├── LocalDataSource  ← Room DB (always active, offline)
 └── RemoteDataSource ← Feature-flagged; disabled by default
```

The phone is always the **source of truth**. Remote sync is a convenience mirror, never required.

See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the full design.

---

## Feature Flags

All optional (potentially paid) services are controlled by a single config file:

**`feature_flags.json`** (repo root; copy to `app/src/main/assets/feature_flags.json` when the Android project is scaffolded)

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

**Default: everything is `false`.** The game works completely without enabling any flag.
Enable only what you need, when you need it. See [`docs/COST_CONTROLS.md`](docs/COST_CONTROLS.md) for free-tier options for each service.

---

## Cost Controls

Running CIVILTAS with **zero ongoing cost** is fully supported. See the dedicated guide:

📄 **[docs/COST_CONTROLS.md](docs/COST_CONTROLS.md)**

Quick summary:
| Service | Default | Free Option |
|---|---|---|
| Data storage | ✅ Room (local) | Room DB — free forever |
| Crash reporting | ❌ disabled | Firebase Crashlytics free tier (when enabled) |
| Analytics | ❌ disabled | Firebase Analytics free tier (when enabled) |
| Cloud sync | ❌ disabled | Firebase Firestore free tier (when enabled) |
| Ads | ❌ disabled | AdMob rewarded ads only (when enabled) |
| Payments | ❌ disabled | Google Play Billing (when enabled) |
| CI security scan | ✅ CodeQL | GitHub CodeQL — free for public repos |

---

## Monetization

CIVILTAS uses a **player-friendly** monetization model:

1. **Season Pass** — Each "Catastrophe Cycle" is a season with a free + paid reward track. Paid track unlocks cosmetics, story chapters, and convenience (not power).
2. **One-time IAP bundles** — Starter pack, cosmetic packs, "Remove Ads" permanent purchase.
3. **Rewarded ads only** — Players *choose* to watch a short ad to double idle collection or skip a small wait. No forced interstitials.
4. **VIP subscription** — Small daily premium currency, extra offline cap, exclusive skins.

**No pay-to-win.** Core progression is always achievable for free.

---

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m "feat: describe what you did"`
4. Push and open a Pull Request targeting `main`.

Please keep all optional/paid service integrations behind the feature flag system described in `docs/COST_CONTROLS.md`.
