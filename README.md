# CIVILTAS 🌍⛏️✨

> *Mine the earth. Rebuild civilization. Uncover ancient secrets.*

CIVILTAS is an idle mining + civilization-rebuilding game for Android. You wake up as a survivor of a catastrophic event, surrounded by the ruins of the old world. Gather resources, rebuild your base, and decode the hidden knowledge left behind by a secret society—while a second catastrophe ticks closer on the horizon.

---

## 🎮 Game Pitch

Humanity survived—but barely. The cataclysm wiped out most of civilization, leaving scattered survivors to start over. You are one of them.

You mine the earth for raw materials (iron, copper, gold, oil, coal, ancient relics…). You rebuild structures. You research lost technologies. And all along, a shadow organization—the **Inner Circle**—watches, feeds you fragments of forbidden knowledge, and hints at what really caused the disaster.

But here's the twist: **another catastrophe is coming**. The question is whether you build for now (maximize your civilization's output) or hoard for the future (prepare a bunker, stockpile supplies). The choice is yours.

---

## 🔄 Core Gameplay Loop

```
Open App
  → Collect offline earnings (idle mining while away)
  → Check Daily Tasks (XP + resource rewards)
  → Upgrade buildings / research tech
  → Decode relics / advance with the Inner Circle
  → Decide: BUILD NOW vs PREPARE FOR CATASTROPHE
  → Close app (mining continues offline)
```

### Resource Tiers

| Tier | Resources | Notes |
|------|-----------|-------|
| Common | Iron, Copper, Coal, Stone, Lumber, Food, Water | Abundant; used for basic building |
| Rare | Gold, Oil | Slower to mine; used for advanced structures |
| Legendary | Ancient Relics, Gnosis | Extremely slow; unlock story content and secret society ranks |

---

## 📈 XP & Progression System

XP is earned by:
- Collecting offline earnings
- Completing daily check-ins and tasks
- Upgrading buildings
- Decoding ancient relics
- Reaching catastrophe preparation milestones

**Level formula:** `XP required = 100 × level × (level + 1) / 2`

Level unlocks:
- **Level 5**: First message from the Inner Circle
- **Level 10**: Vault unlocked (store rare resources safely through catastrophe)
- **Level 20**: Bunker upgrades available
- **Level 30**: Catastrophe forecast mechanic revealed
- **Level 50**: True story ending unlocked

---

## 💥 Catastrophe Cycle

A "Catastrophe Alert" meter (0–10) rises over time. When it hits 10:
- Players who prepared (bunker built, vault stocked) retain a portion of resources
- Players who didn't lose a large portion (soft reset)
- The world rebuilds at a higher tier — new resources and story chapters unlock
- This cycle creates natural retention hooks and gives meaning to both "build" and "hoard" strategies

---

## 💰 Monetization Plan

**Philosophy: earn player loyalty before earning player money.**

### Free-to-play core
- All gameplay is completable for free
- No paywalls blocking story or progression

### Revenue streams

| Stream | Type | Description |
|--------|------|-------------|
| **Season Pass** | Subscription / one-time | Extra daily tasks, cosmetic buildings, bonus resource skin |
| **Resource Bundles** | IAP | Small resource packs (iron/copper starter bundles) — convenience only |
| **Ad Boost** | Optional video ads | Watch an ad → 2× mining rate for 30 min; max 3/day |
| **Cosmetic Skins** | IAP | Visual building skins, character portraits, civilization banners |
| **Skip Timer** | IAP | Skip construction wait (buildings that take hours) |
| **Gnosis Shards** | Premium currency | Earned slowly for free; purchasable for faster rare-resource unlocks |

### What we DON'T do
- No energy systems that hard-block play
- No randomized loot boxes
- No pay-to-win advantages in core progression

---

## 📡 Offline & Online Modes

### Offline
- All gameplay runs locally
- Resources accrue while app is closed (capped at 8 hours)
- Room DB + DataStore for 100% offline persistence

### Online (optional)
- Optional account login (stub ready)
- Local-first sync: all writes go to local DB first
- Background sync pushes deltas to server when connected
- Conflict resolution: local data wins unless remote is strictly newer with no local progress since last sync

### DeNet Storage (future / backlog)
- Roadmap: use [DeNet](https://denet.pro/) decentralized storage
- Player state stored as encrypted blob at player's own DeNet address
- No server-side storage costs; players own their data
- Blocked on: DeNet SDK maturity + compliance review

---

## 🐛 Diagnostics ("Bug Catcher & Destroyer")

- **Timber** for structured logging across all layers
- **In-app Diagnostics screen**: browse last 500 log entries, filter by severity
- **Export/share logs**: one-tap bug report via Android Share Sheet
- **Crash capture**: production ReleaseTree forwards WARN/ERROR to crash reporter (Crashlytics / Sentry integration point ready)
- Logs auto-pruned after 7 days

---

## 💳 Payments Architecture

### Google Play (primary distribution)
- **Google Play Billing Library** for all in-app purchases
- Required when distributing on Google Play Store
- Covers: resource bundles, season pass, cosmetics, Gnosis Shards

### Direct APK / free app sites (initial distribution)
- **Stripe SDK** for card payments (not subject to Play Billing requirement)
- Document: Stripe has its own compliance requirements (PCI-DSS handled by Stripe.js/SDK)

### Crypto payments (roadmap)
- Approach: generate a payment address per order; verify on-chain confirmation
- Supported chains (planned): Bitcoin, Ethereum, Polygon
- Compliance: no custodial handling; player sends from their own wallet
- **Not yet implemented**; marked as backlog

---

## 💰 Wallet Architecture

- **Connect external wallet**: WalletConnect deep-link (stub in current build)
- **Cold wallet reference**: display-only; user enters their cold wallet address for reference
- **Never stores private keys or seed phrases** — hard constraint
- Disclaimer shown prominently in UI

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| DI | Hilt |
| Local DB | Room |
| Preferences | DataStore |
| Navigation | Navigation Compose |
| Async | Coroutines + Flow |
| Logging | Timber |
| Background | WorkManager |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

---

## 📁 Project Structure

```
app/src/main/java/com/civiltas/app/
├── CiviltasApplication.kt       # App entry, Timber/Hilt init
├── MainActivity.kt
├── data/
│   ├── local/                   # Room DB, DAOs, entities
│   ├── repository/              # GameRepository (idle engine)
│   └── sync/                    # SyncRepository (local-first)
├── domain/model/                # Domain types: Resource, Player, Building…
├── diagnostics/                 # CiviltasLogger
├── ui/
│   ├── navigation/              # Nav graph + bottom bar
│   ├── screens/                 # HomeScreen, MiningScreen, BuildingScreen…
│   ├── theme/                   # Material3 theme (dark earth tones)
│   └── viewmodel/               # GameViewModel, WalletViewModel…
└── di/                          # Hilt modules
```

---

## 🚀 Build & Run

```bash
# Clone
git clone https://github.com/jru727ab-cpu/Sovereign-.git
cd Sovereign-

# Debug APK
./gradlew assembleDebug

# Unit tests
./gradlew test
```

Open in Android Studio (Hedgehog or newer). Requires JDK 17.

---

## 📋 Roadmap

- [ ] Full WalletConnect integration
- [ ] Google Play Billing integration
- [ ] Stripe payment flow (direct APK)
- [ ] Online account + sync backend
- [ ] DeNet decentralized storage
- [ ] Catastrophe event full implementation
- [ ] Secret society narrative system
- [ ] Push notifications for mining timers
- [ ] Leaderboards
- [ ] Seasonal events
