# CIVILTAS — Idle Civilization Builder for Android

> *Build. Research. Endure. Rise.*

CIVILTAS is an offline-first idle/incremental civilization game for Android. Players mine resources, construct buildings, advance through a Gnosis/Secret Society knowledge tree, and weather periodic *Catastrophe Cycles* — all without requiring a network connection.

---

## Table of Contents

1. [Core Game Loop](#core-game-loop)
2. [MVP Scope](#mvp-scope)
3. [Architecture Overview](#architecture-overview)
4. [Monetization Strategy](#monetization-strategy)
5. [Monetization Guardrails](#monetization-guardrails)
6. [Distribution Plan](#distribution-plan)
7. [Roadmap](#roadmap)
8. [Development Setup](#development-setup)

---

## Core Game Loop

```
Mine → Collect → Refine → Build → Research → Unlock → Repeat
```

| Track | Purpose |
|-------|---------|
| **Civilization** | Buildings, production chains, logistics |
| **Gnosis / Secret Society** | Knowledge unlocks new *mechanics* (not just stat bumps) |
| **Catastrophe Cycle** | Periodic forced-choice events; drives the season pass |

Sessions are **hybrid** — short actions (seconds) combined with long background idle progress (hours). This keeps players engaged at any session length and maximises monetization touchpoints without annoying them.

---

## MVP Scope

Full detail: [docs/MVP_PLAN.md](docs/MVP_PLAN.md)

**In MVP (v0.1)**

- Offline idle core loop (mine → collect → refine → build)
- XP / level progression
- Catastrophe forecast meter (offline, local)
- Daily objectives + streak (with forgiveness)
- Rewarded-ad hook (interface only, not wired to a network SDK yet)
- One-time "Remove Ads" IAP hook (interface only)
- "Guest mode" that persists save locally forever
- "Create account / Backup" placeholder screen

**Explicitly deferred (not in MVP)**

- Live network sync / cloud backup (Phase 2)
- Stripe / card payments (Phase 2 — APK distribution)
- Google Play Billing wired implementation (Play Store release)
- Crypto payments (Phase 3 — APK only, behind `PaymentProvider` interface)
- Wallet screen / WalletConnect integration (Phase 3)
- Multiplayer / trading (Phase 4+)
- Subscription/VIP wired billing (Phase 2, after Play Store setup)

---

## Architecture Overview

```
app/
├── monetization/          ← payment + ad + IAP interfaces and no-op defaults
│   ├── MonetizationProvider.kt
│   ├── AdProvider.kt
│   ├── PaymentProvider.kt
│   ├── IapProvider.kt
│   └── NoOpMonetizationProvider.kt
├── game/                  ← core game logic (offline-safe)
├── ui/                    ← Jetpack Compose screens
├── data/                  ← local Room DB + save/load
└── sync/                  ← optional cloud sync (stub, disabled by default)
```

All monetization is behind **provider interfaces**. The MVP ships with `NoOpMonetizationProvider` (everything returns "not available"). Swapping in Google Play Billing, Stripe, or a crypto adapter later requires only a new implementation — not a rewrite.

---

## Monetization Strategy

Full detail: [docs/MONETIZATION.md](docs/MONETIZATION.md)

### Supported revenue avenues (all preserved, staged by distribution phase)

| Avenue | Phase | Notes |
|--------|-------|-------|
| **Rewarded ads** (optional) | MVP hook / Phase 2 wired | Player-chosen; doubles idle collection, instant-finishes small tasks |
| **One-time "Remove Ads" purchase** | Phase 2 | Removes all ad prompts; adds small daily freebie |
| **Starter pack / IAP bundles** | Phase 2 | Starter pack, resource logistics pack, cosmetic packs |
| **Subscription / VIP** | Phase 2 | Convenience + cosmetics + higher offline cap; not pay-to-win |
| **Season Pass (Catastrophe Cycle)** | Phase 2 | Free track + premium track per catastrophe season |
| **Cosmetics** | Phase 2 | Themes, UI skins, base visuals, special relic effects |
| **Card payments (Stripe)** | Phase 2 — APK only | Direct APK sites; never required for Play Store build |
| **Google Play Billing** | Phase 2 — Play Store build | Required for all digital goods on Play Store |
| **Crypto payments** | Phase 3 — APK only | Outside Play build; behind `PaymentProvider` interface |

### Principles

- Players can always enjoy the full core game loop **for free**.
- Paid items add **convenience and cosmetics** — never a power spike that breaks balance.
- Ads are **always optional** and player-triggered (rewarded only).
- Every revenue lever has a corresponding interface stub in the codebase from day one.

---

## Monetization Guardrails

See [docs/MONETIZATION.md § Guardrails](docs/MONETIZATION.md#guardrails) for the full policy.

**TL;DR**

1. **No forced ads.** Players opt in to rewarded ads for bonuses. No interstitials, no banner spam.
2. **No pay-to-win power spikes.** Paid items must not break the balance enjoyed by free players.
3. **Keep gameplay fun first.** If a monetization mechanism would make the game feel unfair or grindy, it's out.
4. **Transparency.** Prices are shown clearly. No dark patterns (fake timers, confusing currency conversion, loot boxes without disclosed odds).
5. **Offline always works.** No monetization mechanic can block offline play.

---

## Distribution Plan

1. **Free APK sites** (sideload) — MVP launch, no Play Store review needed.
   - Card payments (Stripe) available.
   - Crypto payments available (Phase 3).
2. **Google Play Store** — after MVP stabilises.
   - Must use Google Play Billing for all digital goods.
   - Crypto/card payment flows disabled in the Play build (feature flag).
3. **Direct website** — optional later.

---

## Roadmap

| Phase | Focus |
|-------|-------|
| **v0.1 MVP** | Offline core loop, monetization interfaces (no-op) |
| **v0.2** | Wired rewarded ads, Remove Ads IAP, starter pack |
| **v0.3** | Subscription/VIP, season pass, cosmetics |
| **v0.4** | Cloud save/sync (opt-in), account system |
| **v0.5** | Play Store release (Google Play Billing) |
| **v1.0** | Crypto payments (APK build), wallet connect |

---

## Development Setup

> Full Android SDK required (API 26+). Kotlin + Jetpack Compose.

```bash
# Clone and open in Android Studio, or build from CLI:
./gradlew assembleDebug

# Run unit tests:
./gradlew test
```

**Requirements:** Android SDK 26+, JDK 17+, Gradle 8+.
