# CIVILTAS — Technical Architecture

## Architecture Overview

CIVILTAS follows **Clean Architecture** with three primary layers:

```
┌─────────────────────────────────────┐
│              UI Layer               │  Jetpack Compose screens + ViewModels
├─────────────────────────────────────┤
│            Domain Layer             │  Pure Kotlin: engines, models, rules
├─────────────────────────────────────┤
│             Data Layer              │  Room DB, DataStore, Repository
└─────────────────────────────────────┘
```

---

## Layer Details

### Domain Layer

The domain layer contains **zero Android dependencies**. It is pure Kotlin and fully unit-testable without an emulator.

Key components:
- **`GameState`** — The canonical game state data class. Single source of truth.
- **`OfflineProgressEngine`** — Computes offline gains from elapsed time
- **`ResourceEngine`** — Computes production rates including skill multipliers
- **`CatastropheEngine`** — Advances the seasonal threat meter
- **`QuestEngine`** — Quest state, completion checks, claim logic

All engines are **pure functions** (objects with static methods) that take `GameState` in and return `GameState` out. No side effects, no coroutines, fully testable.

### Data Layer

- **Room Database** (`CiviltasDatabase`): Single table (`game_state`) persisting a flattened `GameStateEntity`. List fields (activeQuestIds, etc.) are stored as CSV strings for simplicity.
- **`GameRepository`**: Single point of access to local persistence. Maps between `GameStateEntity` and `GameState`.
- **DataStore** (reserved): Will be used for user preferences (notification settings, theme preferences) in Phase 2.

### UI Layer

- **`GameViewModel`**: Holds the `StateFlow<GameState>` and runs the 1-second game tick via a coroutine job. Bridges domain engines with UI.
- **Screens**: Each screen is a stateless Composable that receives `GameViewModel` and `NavController`. No direct state management in screens.
- **Navigation**: Single `NavHost` with a bottom navigation bar. Each destination gets the shared `GameViewModel` via `hiltViewModel()`.

---

## Dependency Injection (Hilt)

Hilt manages the object graph with three modules:

| Module | Provides |
|--------|----------|
| `DatabaseModule` | `CiviltasDatabase`, `GameStateDao` |
| `AccountBindingModule` | `AccountModule` → `LocalGuestAccountModule` |
| `SyncModuleBinding` | `SyncModule` → `NoOpSyncModule` |

The `AccountModule` and `SyncModule` interfaces are designed to be swappable — in Phase 2, real implementations can be bound without changing any caller code.

---

## State Management

```
GameViewModel
  ├── StateFlow<GameState>  (observed by all screens)
  ├── StateFlow<OfflineGains?>  (offline popup trigger)
  └── coroutine tick (1s interval)
       ├── ResourceEngine.computeRates()
       ├── CatastropheEngine.tick()
       └── ResourceEngine.levelFromXp()
```

The game state is **never directly mutated** — all changes go through `GameState.copy()`. This makes debugging straightforward (every state transition is a new snapshot).

---

## Persistence Strategy

The game auto-saves on:
1. `onCleared()` — ViewModel cleanup (app backgrounded/killed)
2. Quest claims and daily check-ins
3. (Phase 2) Every 30 seconds via a periodic save job

On launch, `GameViewModel.init` loads the saved state, computes offline gains, applies them, and starts the tick loop.

---

## Offline Architecture

```
onResume → load GameState
         → compute elapsed = now - lastSeenEpoch
         → cappedElapsed = min(elapsed, offlineCap)
         → apply gains = rates × cappedElapsed
         → show "Welcome Back" dialog if gains > threshold
         → start tick loop
```

The offline cap prevents abuse (setting the device clock forward) and keeps the offline reward feeling generous but bounded.

---

## Feature Flags

BuildConfig fields control optional features:

```kotlin
BuildConfig.ONLINE_SYNC_ENABLED  // false by default
BuildConfig.ADS_ENABLED          // false by default
```

These are checked at runtime in `SettingsScreen` to show/hide relevant UI. The sync and account modules are `NoOp` implementations when disabled.

---

## Threading Model

- **Main thread**: UI, Compose recomposition
- **Coroutine dispatcher (IO)**: Room database operations via `GameRepository`
- **Coroutine dispatcher (Main)**: `StateFlow` updates in `GameViewModel`
- **No manual thread management**: All async work uses `viewModelScope.launch` and Room's built-in coroutine support

---

## Testing Strategy

### Unit Tests (JVM — no Android required)
Located in `app/src/test/`:
- `OfflineProgressEngineTest` — Gain computation, cap enforcement
- `ResourceEngineTest` — Rate computation, XP/level math
- `CatastropheEngineTest` — Progress advancement, season reset, labels

### Instrumented Tests (Phase 2)
Will cover:
- Room DAO operations
- ViewModel integration with fake repository
- Compose UI smoke tests

---

## Future Architecture (Phase 2)

- **WorkManager**: Background periodic saves, daily quest reset
- **DataStore**: User preferences (theme, notifications)
- **Firebase/Supabase**: Optional cloud sync behind `SyncModule`
- **Play Games Services**: Leaderboards and achievements
- **Crashlytics**: Production crash reporting (replaces in-app `CiviltasLogger` for fatal errors)
