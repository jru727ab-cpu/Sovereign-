# CIVILTAS Technical Architecture

## Overview

CIVILTAS follows a clean architecture pattern:
- **Data layer**: Room DB, DataStore, Sync layer
- **Domain layer**: Pure Kotlin models and use cases
- **Presentation layer**: Jetpack Compose screens + Hilt ViewModels

## Offline-First Architecture

```
┌─────────────────────────────────┐
│           UI Layer              │
│    (Compose + ViewModels)       │
└────────────┬────────────────────┘
             │ StateFlow / SharedFlow
┌────────────▼────────────────────┐
│        GameRepository           │
│  (idle engine, XP, buildings)   │
└──────┬──────────────────────────┘
       │
┌──────▼──────┐    ┌──────────────┐
│  Room DB    │    │  DataStore   │
│  (entities) │    │  (prefs)     │
└──────┬──────┘    └──────────────┘
       │
┌──────▼──────────────────────────┐
│       SyncRepository            │
│   (local-first, push/pull)      │
└──────────────────────────────────┘
              │ (future)
┌─────────────▼───────────────────┐
│   Remote Backend / DeNet        │
└─────────────────────────────────┘
```

## Idle Mining Engine

The idle engine is implemented in `GameRepository.applyOfflineEarnings()`:
1. On app start, compute elapsed time since `lastActiveTimestamp`
2. Cap at `MAX_OFFLINE_HOURS` (8h) to prevent extreme accumulation
3. For each resource, apply `rate × elapsedHours` to stored amount
4. Award XP proportional to elapsed time
5. Update `lastActiveTimestamp`

## Sync Strategy (Local-First)

```
Write → Local DB (immediate)
      → Queue sync job (WorkManager)
      → Push to remote when online
      → On conflict: local wins if local.timestamp > remote.timestamp
                     remote wins if remote is strictly newer AND no local edits since last sync
```

## Diagnostics Architecture

```
User Action / System Event
  → Timber.log()
  → CiviltasLogger (persists to diagnostic_logs table)
  → DiagnosticsScreen (observes via Flow)
  → Export: plain text via Android Share Sheet
```

Crash capture integration point: `ReleaseTree` in `CiviltasApplication` — forward to
Firebase Crashlytics or Sentry by adding the SDK and calling their APIs there.

## Wallet Security Model

- **No private keys ever enter the app**
- Cold wallet address: stored in DataStore as display-only text
- Connected wallet: address only (public key), obtained via WalletConnect handshake
- All signing happens in the user's external wallet app
- Future WalletConnect integration: use official WalletConnect Android SDK

## DeNet Storage (Future)

DeNet is a decentralized storage protocol where users own their data:
- Player state serialized to JSON + encrypted with user's key
- Stored at the player's personal DeNet storage address
- No central server needed
- Implementation blocked on: DeNet Android SDK availability + legal review of user data storage in target markets

## Payments

| Distribution | Payment Method | Notes |
|-------------|---------------|-------|
| Google Play | Play Billing Library | Mandatory per Google policy |
| Direct APK | Stripe Android SDK | PCI-DSS handled by Stripe |
| Crypto | On-chain verification | Backlog; no custodial handling |

## Security Checklist

- [ ] No private keys in codebase or DataStore
- [ ] ProGuard enabled for release builds
- [ ] Network requests over HTTPS only (enforced via Network Security Config)
- [ ] Exported activities minimized (only MainActivity exported)
- [ ] Sensitive user data (wallet address) never sent to analytics
- [ ] Crash reports anonymized before upload

## Build & Test

```bash
./gradlew assembleDebug      # Debug APK
./gradlew assembleRelease    # Release APK (requires signing config)
./gradlew test               # Unit tests
./gradlew connectedCheck     # Instrumented tests (requires emulator/device)
```
