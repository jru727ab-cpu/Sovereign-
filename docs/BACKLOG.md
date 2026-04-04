# CIVILTAS — Backlog (deferred from MVP)

Items here are **explicitly out of scope for v0.1**.  
Add them in the order that delivers player value most efficiently.

---

## Phase 2 — Online / Cloud

| Item | Notes |
|------|-------|
| Cloud save sync | Implement `SyncRepository`; offline path unchanged. Start with Firebase or simple REST. |
| DeNet / IPFS storage adapter | Implement `StorageBackend` interface. Swap in without touching `GameEngine`. |
| Server-side validation | Only needed if trading / leaderboards are added. |

## Phase 2 — Payments & Monetisation

| Item | Notes |
|------|-------|
| Google Play Billing | Implement `PlayBillingProvider : PaymentProvider`. Required for Play Store release. |
| Stripe / card payments (APK builds) | Only for sideload channel; never mix with Play Billing. |
| Crypto payments (WalletConnect) | Implement `CryptoPaymentProvider`. Only for sideload channel. |
| Cold wallet integration | Display-only: QR code + watch-only address. No private keys in app. |
| Season Pass IAP | Requires Play Billing. Design season schedule first. |
| "Remove Ads" one-time purchase | Simple boolean flag persisted in `SaveManager`. |

## Phase 2 — Content & Gameplay Depth

| Item | Notes |
|------|-------|
| Full building tree (10+ buildings) | Define in a data file (JSON/sealed class); engine already supports building hooks. |
| Knowledge / research tree | Unlock tech bonuses + story. |
| Secret Society reveal (story chapters) | Gated behind XP milestones; write lore first. |
| Catastrophe event mechanics | Resource reset rules, survival bonuses, "hoard vs build" decision. |
| Expedition system | Timed runs for rare resources. |
| Daily quests + streak system | XP bonuses; use local notifications. |

## Phase 3 — Multiplayer / Community

| Item | Notes |
|------|-------|
| Leaderboards | Firebase or Play Games SDK. |
| Trading / marketplace | Needs server-side anti-cheat. High complexity — defer until player base exists. |
| Alliance / faction system | Major feature; plan separately. |

## Phase 3 — Diagnostics & Ops

| Item | Notes |
|------|-------|
| Crash reporting SDK | Add Firebase Crashlytics (one Gradle line) when ready to invest in crash monitoring. |
| Analytics | Firebase Analytics behind a privacy-consent flag. Off by default. |
| Performance monitoring | Add only if frame-drop complaints arise. |

## Phase 3 — Platform & Distribution

| Item | Notes |
|------|-------|
| Google Play Store release | Requires Play Billing, privacy policy, signed release APK. |
| iOS port (Kotlin Multiplatform or React Native) | Defer until Android v1 is stable. |
| Push notifications | Local first (WorkManager); remote later (FCM). |

---

> **Rule**: Before pulling anything from this backlog, confirm it delivers measurable player value
> and does not require rewriting existing code. Use the extension points described in README.md.
