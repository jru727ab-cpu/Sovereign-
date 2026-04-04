# CIVILTAS

> *Build. Survive. Ascend.*

CIVILTAS is an **offline-first idle mining & civilization game** for Android. You are a Sovereign forging a civilization from raw earth — mining ore, carving stone, accumulating knowledge, and racing against an ever-approaching catastrophe that threatens to reset everything you've built.

---

## 🎮 Game Pitch

You wake up as the last Sovereign of a forgotten age. Your civilization lies in ruins. Armed only with a pickaxe and the memory of ancient knowledge, you must rebuild — one ore vein at a time.

- **Mine** resources passively, even while offline
- **Upgrade** your extraction machinery to scale production exponentially  
- **Unlock skills** across three trees: Miner, Builder, Scholar
- **Complete quests** for bonus resources and skill points
- **Survive the Catastrophe** — a seasonal threat that pressures you to grow faster
- **Ascend** through Gnosis ranks to unlock deeper mechanics

---

## 📦 Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Architecture | Clean Architecture (data / domain / ui) |
| DI | Hilt |
| Database | Room |
| Preferences | DataStore |
| Navigation | Navigation Compose |
| Async | Coroutines + Flow |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

---

## 🏗 Project Structure

```
app/src/main/java/com/sovereign/civiltas/
├── CiviltasApplication.kt          # Hilt entry point
├── MainActivity.kt
├── account/                        # Auth abstraction (guest-first)
│   ├── AccountModule.kt
│   └── LocalGuestAccountModule.kt
├── data/
│   ├── local/db/                   # Room database
│   │   ├── CiviltasDatabase.kt
│   │   ├── dao/GameStateDao.kt
│   │   └── entity/GameStateEntity.kt
│   └── repository/GameRepository.kt
├── di/AppModule.kt                 # Hilt DI bindings
├── diagnostics/                    # In-game logging & bug reporting
│   ├── CiviltasLogger.kt
│   └── BugReporter.kt
├── domain/
│   ├── engine/                     # Pure game logic (no Android deps)
│   │   ├── CatastropheEngine.kt
│   │   ├── OfflineProgressEngine.kt
│   │   ├── QuestEngine.kt
│   │   └── ResourceEngine.kt
│   └── model/                      # Domain data classes
│       ├── GameState.kt
│       ├── Quest.kt
│       ├── Resource.kt
│       ├── Skill.kt
│       └── Upgrade.kt
├── sync/                           # Online sync abstraction (NoOp by default)
│   ├── NoOpSyncModule.kt
│   └── SyncModule.kt
└── ui/
    ├── navigation/CiviltasNavGraph.kt
    ├── screens/
    │   ├── HomeScreen.kt
    │   ├── QuestsScreen.kt
    │   ├── SettingsScreen.kt
    │   ├── SkillsScreen.kt
    │   └── UpgradesScreen.kt
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    └── viewmodel/GameViewModel.kt
```

---

## 🚀 MVP Scope (v0.1.0)

- [x] Idle resource generation (ore, stone, knowledge, energy)
- [x] Offline progress calculation with configurable cap
- [x] Manual ore mining (tap to mine)
- [x] Three upgradeable resource extractors
- [x] Skill tree system (Miner / Builder / Scholar trees)
- [x] Daily check-in with streak bonuses
- [x] Daily + Rotating quest system
- [x] Catastrophe seasonal cycle with threat meter
- [x] Gnosis rank progression track
- [x] XP + level system with skill point rewards
- [x] Guest mode (no account required)
- [x] Room persistence (save/load game state)
- [x] In-game diagnostics + bug reporter
- [x] Dark theme with CIVILTAS design language

---

## 🚫 Non-Goals (Phase 1)

- Real-time multiplayer
- Cloud save / account login (stubs exist, disabled by default)
- In-app purchases / ads (flags exist in BuildConfig, disabled)
- Push notifications
- Social features

---

## 💰 Monetization Philosophy

CIVILTAS is designed to be **completely playable offline without spending**. Monetization is friction-free:

- **No pay-to-win** — purchasable items are cosmetic or QoL, never game-breaking
- **No forced ads** — ads are opt-in for resource boosts
- **No energy gates** — energy is a resource, not a paywall
- See [`docs/MONETIZATION.md`](docs/MONETIZATION.md) for full philosophy

---

## 🛠 Building

```bash
# Requires Android SDK (set in local.properties)
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

---

## 📚 Design Docs

- [Progression Design](docs/PROGRESSION.md)
- [Economy Design](docs/ECONOMY.md)  
- [Catastrophe Cycle](docs/CATASTROPHE_CYCLE.md)
- [Monetization Philosophy](docs/MONETIZATION.md)
- [Technical Architecture](docs/TECHNICAL_ARCHITECTURE.md)
