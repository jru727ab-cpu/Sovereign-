# CIVILTAS — Android MVP Plan

This document defines the exact scope of the v0.1 MVP: what is built now, what is deliberately deferred, and why.

---

## Contents

1. [MVP Goal](#mvp-goal)
2. [Session Cadence](#session-cadence)
3. [In Scope (v0.1)](#in-scope-v01)
4. [Out of Scope — Deferred](#out-of-scope--deferred)
5. [Package & Tech Stack](#package--tech-stack)
6. [File Structure](#file-structure)
7. [Build & Test](#build--test)

---

## MVP Goal

Ship a **fun, fully offline, self-contained idle civilization game** that:

1. Demonstrates the core loop end-to-end (mine → collect → refine → build → research → repeat).
2. Has a working Catastrophe Cycle forecast so the "season pass" concept is visible to early players.
3. Keeps all monetization, sync, and wallet features behind **interface stubs** so Phase 2 additions require no game-logic rewrites.
4. Is ready to distribute as a signed APK on free app sites.

---

## Session Cadence

**Hybrid** (best for idle games with high retention):

- **Short actions** (5–30 seconds): tap to collect, start a build/research, claim a daily objective.
- **Long idle periods** (1–8 hours): production continues offline; returning player collects accumulated resources.
- **Event windows** (Catastrophe Cycle): meaningful choices every few days of real time.

This cadence maximises session frequency (more ad/IAP opportunities) while never requiring players to be glued to the screen.

---

## In Scope (v0.1)

### Core Loop
- [x] Resource nodes (mine, refinery, storage)
- [x] Idle production timer (runs in background via `WorkManager` or foreground service)
- [x] Collect action (tap to harvest accumulated resources)
- [x] Building construction (queued, time-based)
- [x] Research tree (unlocks new resource types or mechanics)
- [x] XP / Level system (milestones unlock new content tiers)

### Progression Tracks
- [x] **Civilization track**: buildings → production chains → logistics
- [x] **Gnosis / Secret Society track**: knowledge nodes that unlock new *mechanics* (not just stat bumps)

### Catastrophe Cycle
- [x] Local forecast meter (fills passively over time)
- [x] Threshold event: player must make a forced choice when meter fills
- [x] Outcome affects resource stockpiles and building state
- [x] No network required; seed determined by local time

### Retention Hooks
- [x] Daily objectives (3–5 rotating tasks)
- [x] Login streak tracker
- [x] Streak forgiveness token (1 free miss per 7 days, or spend a small in-game token)
- [x] "First time back" collection moment — animated resource harvest

### Monetization Interfaces (hooks only — no money flows)
- [x] `MonetizationProvider` façade interface
- [x] `AdProvider` interface (rewarded ad lifecycle)
- [x] `IapProvider` interface (one-time purchases, subscriptions, season pass)
- [x] `PaymentProvider` interface (card/crypto external payments)
- [x] `NoOpMonetizationProvider` default implementation (returns "unavailable" for everything)
- [x] Rewarded-ad prompt UI component (calls `AdProvider.showRewardedAd()`; shows "not available" in MVP)
- [x] "Remove Ads" button on ad prompts (calls `IapProvider.purchaseRemoveAds()`; no-op in MVP)
- [x] "VIP / Patron" teaser screen (calls `IapProvider.querySubscriptions()`; shows "coming soon" in MVP)

### Save System
- [x] Local save (Room DB or JSON file in internal storage)
- [x] Auto-save every 30 seconds + on app pause
- [x] Safe-mode boot: if app crashes 3× in a row, boot in minimal mode with repair options

### Diagnostics / Bug Catcher
- [x] Local crash log (caught exceptions written to internal log file)
- [x] "Report a Problem" screen: exports log + device info as a shareable text file
- [x] Frame-rate / ANR monitoring (in-game performance overlay, debug builds only)

### Placeholder Screens
- [x] "Create Account / Backup Save" (UI only; no backend wired)
- [x] "Wallet" placeholder (UI only; explains connect-external / watch-only concept)
- [x] "Shop" screen (shows product stubs; all marked "coming soon")

---

## Out of Scope — Deferred

These are explicitly **not** in v0.1 to keep the build lean.

| Feature | Target Phase | Reason Deferred |
|---------|-------------|-----------------|
| Live network sync / cloud backup | Phase 2 | Requires backend; adds cost and complexity |
| Rewarded ad SDK (AdMob etc.) | Phase 2 | No network dependency in MVP |
| Google Play Billing wired | Phase 2 / Play Store | Requires Play Console setup |
| Stripe card payments | Phase 2 — APK only | Compliance + PCI scope; not needed pre-launch |
| Crypto payments | Phase 3 — APK only | Compliance review needed; behind `PaymentProvider` interface |
| WalletConnect / wallet screen | Phase 3 | Security-sensitive; design must be reviewed |
| Private key generation/storage | **Never** | Security and trust risk; use connect-external/watch-only only |
| Multiplayer / trading | Phase 4+ | Requires anti-cheat, backend, significant scope |
| Season pass (wired billing) | Phase 2 | Catastrophe cycle exists in MVP; billing wired later |
| Subscription/VIP (wired billing) | Phase 2 | Teaser screen exists in MVP |
| Decentralised storage (IPFS/Arweave) | Phase 4+ | DeNet-type layer is an optional future adapter |
| Analytics SDK | Phase 2 | Privacy-first; off by default; add when needed |
| Push notifications | Phase 2 | Offline-first; idle collection works without it |

---

## Package & Tech Stack

| Item | Value |
|------|-------|
| Package name | `com.civiltas.app` |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 |
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt (or manual DI for MVP if Hilt adds complexity) |
| Local DB | Room (game state) |
| Background work | WorkManager (idle production ticks) |
| Build tool | Gradle 8+ |

---

## File Structure

```
app/
├── src/main/java/com/civiltas/app/
│   ├── monetization/          ← provider interfaces + no-op defaults (MVP deliverable)
│   │   ├── MonetizationProvider.kt
│   │   ├── AdProvider.kt
│   │   ├── IapProvider.kt
│   │   ├── PaymentProvider.kt
│   │   └── NoOpMonetizationProvider.kt
│   ├── game/
│   │   ├── GameEngine.kt       ← idle tick logic
│   │   ├── ResourceManager.kt
│   │   ├── BuildingManager.kt
│   │   ├── ResearchTree.kt
│   │   └── CatastropheCycle.kt
│   ├── data/
│   │   ├── GameDatabase.kt     ← Room DB
│   │   ├── SaveManager.kt      ← auto-save + safe-mode boot
│   │   └── models/
│   ├── diagnostics/
│   │   ├── CrashLogger.kt
│   │   └── PerformanceMonitor.kt
│   ├── ui/
│   │   ├── MainScreen.kt
│   │   ├── ShopScreen.kt       ← "coming soon" stubs
│   │   ├── WalletScreen.kt     ← placeholder
│   │   └── DiagnosticsScreen.kt
│   └── sync/
│       └── SyncManager.kt      ← stub, disabled by default
docs/
├── MONETIZATION.md             ← this repo's monetization policy
└── MVP_PLAN.md                 ← this file
README.md
```

---

## Build & Test

```bash
# Debug build
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run connected (device/emulator) tests
./gradlew connectedAndroidTest
```

**No network or paid services are required to build or test the MVP.**
