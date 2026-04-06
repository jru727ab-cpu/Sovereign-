# CIVILTAS — Sovereign Command

> *"The Archive does not fear the coming storm. It has already survived the last one."*
> — Directorate Communiqué, Cycle 7

---

## What is CIVILTAS?

**CIVILTAS** is a hybrid idle/incremental strategy game set in the aftermath of a catastrophic collapse. You are a curator of survival — managing resource extraction, civilisation infrastructure, and the sacred knowledge of a secret society working to outlast the **next** catastrophe before it arrives.

The tone is **hybrid**: grounded sci-fi meets esoteric mysticism. The Directorate of Archivists catalogues cold hard data; the Order of the Eternal Compass interprets the geometry beneath reality. Both know the same truth: *something is coming again, and the only question is whether you'll be ready.*

---

## Core Gameplay Loop

```
Mine → Collect → Refine → Build → Unlock Secrets → Survive → Repeat
```

**Two progression tracks run in parallel:**

| Track | Focus | Flavour |
|---|---|---|
| **Civilisation Track** | Buildings, production chains, logistics | "Build the infrastructure to endure." |
| **Gnosis Track** | Secrets, knowledge unlocks, society rank | "Understand why so you can survive the how." |

---

## The Catastrophe Cycle (Retention Engine)

A **Forecast Meter** tracks the probability of the next catastrophe. It is never a perfect timer — it is uncertainty made visible. Players who collect **Secrets** increase their forecast confidence, narrowing the uncertainty window and giving themselves more time to prepare.

When the catastrophe strikes:
- **Archivists** (knowledge-focused) unlock hidden survival options
- **Hoarders** (resource-focused) weather shortages better
- **Builders** (output-focused) recover fastest — if they survive at all

There is no wrong path. Each philosophy changes *how* the catastrophe hits you and *what* options you have coming out the other side.

---

## The Secrets System

**Secrets** are collectible knowledge cards unlocking lore, strategic intelligence, resource efficiency, and sacred geometry blueprints. They sit at the intersection of narrative, progression, and monetisation.

### Categories

| Category | Flavour | Effect |
|---|---|---|
| **Directorate Intel** | Cold-case data files from the Archivists | Forecast confidence, resource maps |
| **Order Geometry** | Sacred diagrams and encoded glyphs | Blueprint unlocks, hidden building layouts |
| **Survivor Testimony** | Fragmented accounts from the last collapse | Lore reveals, morale bonuses |
| **Continuity Protocols** | Pre-collapse contingency plans | Disaster mitigation, evacuation options |

### Earning Secrets (free paths)

- Complete **daily task milestones** (streak-based)
- Reach **expedition milestones** (explore regions)
- Advance through **Gnosis research** branches
- Unlock via **society rank-ups**

### Accelerating via Store (optional, ethical)

- **Secrets packs** — lore bundles, intel dossiers (story + light utility)
- **Season Pass** — premium track per catastrophe cycle (cosmetics + early access)
- **VIP Subscription** — convenience + cosmetics + better offline cap
- **Optional rewarded ads** — player-initiated only, no forced interstitials

> **Guardrail:** Every meaningful progression path is earnable for free. Paid purchases provide faster access, cosmetics, extra story depth, and convenience — never an exclusive hard gate on survival.

---

## Monetisation at a Glance

```
Season Pass (per cycle) ← strongest long-term revenue
VIP Subscription        ← recurring
IAP Secrets Packs       ← one-time / repeatable
Remove Ads              ← one-time
Rewarded Ads            ← player-choice only
```

Payment processing is **stubbed** in the MVP. The `StoreRepository` interface is ready for Google Play Billing (Play Store release) or Stripe/crypto (direct APK distribution). Swap the implementation without touching game logic.

---

## Architecture (Android MVP)

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Persistence:** SharedPreferences (simple local saves; no server dependency)
- **Min SDK:** 26 | **Target SDK:** 34
- **No heavy DI framework** — manual injection kept simple for MVP

### Key packages

```
com.civiltas.app
├── data/
│   ├── model/         # Secret, StoreItem, ForecastState
│   ├── SecretsCatalog # 15 built-in secrets
│   ├── SecretsRepository  # unlock persistence
│   ├── ForecastRepository # confidence meter
│   └── EarningEngine      # milestone tracking
├── ui/
│   ├── screens/       # SecretsLibraryScreen, SecretDetailScreen, StoreScreen
│   ├── theme/         # CIVILTAS dark theme
│   └── navigation/    # NavGraph
└── MainActivity
```

---

## Quick Start (Dev)

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

---

## Docs

- [`docs/secrets.md`](docs/secrets.md) — full Secrets system reference
- [`docs/catastrophe-cycle.md`](docs/catastrophe-cycle.md) — forecast mechanics and cycle design
