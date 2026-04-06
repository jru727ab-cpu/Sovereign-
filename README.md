# CIVILTAS — Android Idle Mining Game

> **Civilization rebuilder. Earth-resource miner. Secret-society mystery.**

You are a survivor of a catastrophic event. Mine metals, gold, and oil. Gather knowledge.
Unlock secrets from a hidden society that knows the *next* catastrophe is coming.
The choice is yours: build for today, or hoard for tomorrow.

---

## MVP Scope (what is in-scope for v0.1)

| Feature | Status |
|---------|--------|
| Offline idle resource accumulation (metals, gold, oil) | ✅ Implemented |
| XP / level-up system | ✅ Implemented |
| Basic building progression (Mine → Refinery → Vault) | ✅ Stub — unlocked by level |
| Persistent local save (SharedPreferences) | ✅ Implemented |
| Single-screen Compose UI (resources + idle timer) | ✅ Implemented |
| Catastrophe cycle countdown (UI teaser) | ✅ Implemented (display only) |
| Secret Society teaser unlock at XP milestone | ✅ Stub behind interface |

## Non-Goals for v0.1 (explicitly deferred — see [BACKLOG](docs/BACKLOG.md))

- DeNet / IPFS / decentralised cloud storage
- Crypto / wallet payments (WalletConnect, cold wallet)
- Card payment processing (Stripe / Play Billing)
- Multiplayer, trading, shared world
- Advanced analytics / crash-reporting SDK
- Server-side game logic or anti-cheat
- Season pass / IAP purchase flows
- Push notifications

---

## Credits / Cost Discipline

This project follows a **lean-MVP discipline** to avoid burning agent/runtime credits on
unnecessary back-and-forth, heavy generated assets, or over-engineering.

### What is implemented now
- `GameEngine` — pure-Kotlin offline idle tick; no network, no background service.
- `SaveManager` — SharedPreferences wrapper; one class, no ORM.
- `MainActivity` — single `@Composable` screen, no Navigation graph for MVP.

### What is stubbed behind interfaces
- `StorageBackend` interface — plug in Room / SQLite later without rewriting business logic.
- `PaymentProvider` interface — plug in Google Play Billing or crypto later.
- Building "unlock" checks — called but return static stubs until content is written.

### What is explicitly postponed
See [docs/BACKLOG.md](docs/BACKLOG.md) for the full prioritised backlog.

### How to extend later without rewriting
1. **Persistence**: replace `SharedPrefsStorageBackend` with `RoomStorageBackend` implementing `StorageBackend`.
2. **Payments**: create `PlayBillingProvider : PaymentProvider` — no other code changes needed.
3. **Online sync**: add a `SyncRepository` called only from a settings toggle; offline path is unchanged.
4. **New resources**: add an entry to `Resource` enum + icon — the UI and engine pick it up automatically.

---

## Architecture (lean)

```
MainActivity (Compose UI)
    └── GameEngine (idle tick, pure Kotlin, no Android deps)
            └── SaveManager (SharedPreferences, injected via interface)
```

No Hilt. No Room. No Navigation graph. No heavy third-party SDKs.

---

## Build

```bash
# Debug APK (sideload on free app sites)
./gradlew assembleDebug

# Unit tests
./gradlew test
```

Minimum SDK: **24** (Android 7.0) — covers ~97% of active devices.
Target SDK: **34**.

---

## Monetisation Plan (v1+ roadmap)

- **Rewarded ads only** — player-initiated, never forced.
- **"Remove Ads" one-time IAP** — replaces ad prompts with small daily freebies.
- **VIP subscription** — higher offline cap, cosmetic themes, queue slots (convenience, not power).
- **Catastrophe Season Pass** — free + paid track per season cycle.
- **No pay-to-win**: all core resources and progression are achievable for free.

---

## Licence

See [LICENCE](LICENCE) (to be added before Play Store release).
