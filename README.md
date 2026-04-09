# CIVILTAS

**Rebuild. Remember. Survive.**

A post-collapse idle strategy game for Android where players mine resources, uncover secrets of the Order of the Compass, and forecast civilizational catastrophe.

---

## MVP Features

- **Idle Mining Loop** — Ore accumulates automatically per second; manual collect button gives instant bonus
- **Upgrade System** — Purchase drill upgrades that increase ore-per-second (exponential cost curve)
- **Offline Progress** — Calculates ore earned since last session (capped at 8 hours)
- **Secrets Library** — 12 data-driven secrets from the "Order of the Compass", filterable by category
- **Catastrophe Forecast Meter** — Tension gauge that improves as secrets are unlocked
- **Bottom Navigation** — Home (Mining), Secrets Library, Store (stub)
- **Persistence** — SharedPreferences + org.json; no external database dependency

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Navigation | Compose Navigation |
| State | ViewModel + StateFlow |
| Persistence | SharedPreferences + org.json |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

---

## Project Structure

```
app/src/main/java/com/civiltas/app/
  MainActivity.kt
  data/
    GameState.kt        # Data models: MiningState, SecretEntry, GameState
    GameRepository.kt   # SharedPreferences persistence layer
  game/
    IdleEngine.kt       # Pure math: offline gain, OPS, upgrade cost
    SecretsData.kt      # 12 secrets definitions (data-driven)
  ui/
    theme/              # Color, Type, Theme (dark — DeepNavy/Gold palette)
    screens/
      GameViewModel.kt  # Shared ViewModel, coroutine tick loop
      HomeScreen.kt     # Mining, collect, upgrade, forecast meter
      SecretsScreen.kt  # Filterable secrets library
      StoreScreen.kt    # IAP stubs (coming soon)
    navigation/
      AppNavigation.kt  # BottomNavigation scaffold
```

---

## Build & Run

```bash
# Prerequisites: Android SDK, JDK 17+
./gradlew assembleDebug
./gradlew test
```

Install on connected device/emulator:
```bash
./gradlew installDebug
```

---

## Docs

- [`docs/MVP_SCOPE.md`](docs/MVP_SCOPE.md) — MVP scope and guardrails
- [`docs/MONETIZATION.md`](docs/MONETIZATION.md) — Monetization strategy
- [`docs/SECRETS_DESIGN.md`](docs/SECRETS_DESIGN.md) — Secrets system design

---

## License

Proprietary — CIVILTAS © 2024. All rights reserved.
