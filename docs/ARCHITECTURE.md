# CIVILTAS — Architecture

## Overview

CIVILTAS is built on an **offline-first, local-source-of-truth** principle.  
The app is fully functional with zero network connectivity. All optional online
services are gated behind feature flags and only activated when explicitly
configured by the operator.

---

## Layered Architecture

```
┌───────────────────────────────────────────────┐
│              UI Layer (Jetpack Compose)        │
│  Screens: Mining, City, Research, Expedition,  │
│           Vault, Settings, Secret Society      │
└────────────────────┬──────────────────────────┘
                     │ StateFlow / UI State
┌────────────────────▼──────────────────────────┐
│           ViewModel Layer (Hilt)               │
│  MiningViewModel, CityViewModel,               │
│  ResearchViewModel, ProgressionViewModel       │
└────────────────────┬──────────────────────────┘
                     │ suspend functions / Flow
┌────────────────────▼──────────────────────────┐
│            Repository Layer                    │
│  ResourceRepository, BuildingRepository,       │
│  ResearchRepository, ProgressionRepository     │
└──────┬─────────────────────────┬──────────────┘
       │                         │  (feature flag check)
┌──────▼──────────┐   ┌──────────▼────────────────┐
│ LocalDataSource │   │    RemoteDataSource        │
│  Room DB        │   │  (DISABLED by default)     │
│  SharedPrefs    │   │  Firebase / DeNet / custom │
│  Assets         │   │  Only active when flag set │
└─────────────────┘   └───────────────────────────┘
```

The **LocalDataSource** is always active.  
The **RemoteDataSource** is instantiated but no-ops until its feature flag is `true`.

---

## Data Layer (Local)

### Room Database
- **Entities:** `ResourceEntity`, `BuildingEntity`, `ResearchEntity`, `ExpeditionEntity`, `ProgressionEntity`
- **DAOs:** typed query interfaces per entity
- **Migrations:** versioned Room migrations; no data loss on upgrade

### Offline Progress Calculation
Idle resource gains are calculated deterministically from the last-seen timestamp:

```kotlin
val elapsedSeconds = (System.currentTimeMillis() - lastSeen) / 1000L
val gained = miningRate * elapsedSeconds
```

This runs entirely on-device with no network call.

### SharedPreferences
Lightweight key/value store for:
- Feature flag overrides
- UI preferences
- Wallet display addresses (public only — never private keys)

---

## Feature Flag System

Feature flags are loaded once at startup from:
1. `feature_flags.json` in the repo root (build-time defaults — all `false`). When the Android project is scaffolded, copy this file to `app/src/main/assets/feature_flags.json`.
2. `SharedPreferences` overrides (runtime operator overrides)

```kotlin
object FeatureFlags {
    var onlineSyncEnabled: Boolean = false
    var crashReportingEnabled: Boolean = false
    var analyticsEnabled: Boolean = false
    var adsEnabled: Boolean = false
    var paymentsEnabled: Boolean = false
    var cloudBackupEnabled: Boolean = false

    fun load(context: Context) {
        val json = context.assets.open("feature_flags.json")
            .bufferedReader().readText()
        val obj = JSONObject(json)
        onlineSyncEnabled    = obj.optBoolean("online_sync_enabled", false)
        crashReportingEnabled = obj.optBoolean("crash_reporting_enabled", false)
        analyticsEnabled     = obj.optBoolean("analytics_enabled", false)
        adsEnabled           = obj.optBoolean("ads_enabled", false)
        paymentsEnabled      = obj.optBoolean("payments_enabled", false)
        cloudBackupEnabled   = obj.optBoolean("cloud_backup_enabled", false)
    }
}
```

Every optional service checks its flag before initialising:

```kotlin
if (FeatureFlags.crashReportingEnabled) {
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
}
```

---

## Diagnostics / "Bug Catcher"

The in-app diagnostics system runs entirely on-device and requires no paid service:

- **Logcat ring-buffer** — last 500 log lines kept in memory, flushed to local file on crash
- **Crash interceptor** — `Thread.setDefaultUncaughtExceptionHandler` writes a crash report to internal storage before the default handler runs
- **Safe-mode boot** — if the app crashes 3× within 60 seconds, it boots in minimal mode and offers "Repair save / Reset UI cache"
- **"Report a Problem" screen** — exports a ZIP of logs + device info + last 10 actions; user explicitly chooses to share

When `crash_reporting_enabled = true`, crash reports are additionally forwarded to Firebase Crashlytics (free tier handles typical indie game volume with no charge).

---

## Wallet Architecture

**Core rule: private keys never touch the app.**

| Feature | Implementation |
|---|---|
| Display receiving address | User pastes public address; stored in local DB |
| QR code display | Generated on-device from stored address |
| Transaction history | Deep-link to block explorer (no API key needed for public chains) |
| Connect external wallet | WalletConnect v2 (open-source, free) |
| Cold wallet guidance | In-app "Cold Wallet Setup" guide with external hardware wallet links |

When `payments_enabled = true`:
- **Google Play Billing** for in-app purchases (required for Play Store distribution)
- **Crypto payments** via WalletConnect for direct APK builds (alternative distribution)

---

## Sync / Cloud Backup

When `online_sync_enabled = true` or `cloud_backup_enabled = true`:

- **Conflict resolution:** last-write-wins on `updatedAt` timestamp; resource totals merge (add, never replace)
- **Sync provider interface:** pluggable backend (Firebase, custom REST, DeNet storage adapter)
- **Privacy:** only game state is synced; no personal data without explicit user consent

---

## CI / CD

| Check | Tool | Cost |
|---|---|---|
| Security scan | GitHub CodeQL | **Free** (public repo) |
| Unit tests | GitHub Actions `ubuntu-latest` | **Free** (2,000 min/month included) |
| Lint | `ktlint` / Android Lint | **Free** |
| Release build | `./gradlew assembleRelease` | **Free** |

All CI checks use only free GitHub-provided runners and free open-source tools.
