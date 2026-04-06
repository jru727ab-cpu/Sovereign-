# CIVILTAS — Monetization Strategy

This document is the authoritative reference for CIVILTAS monetization. It covers every revenue avenue, the staged rollout plan, integration architecture, and the non-negotiable guardrails that protect player experience.

---

## Contents

1. [Design Philosophy](#design-philosophy)
2. [Revenue Avenues](#revenue-avenues)
   - [Rewarded Ads](#1-rewarded-ads-optional)
   - [Remove Ads (one-time purchase)](#2-one-time-remove-ads-purchase)
   - [Starter Packs & IAP Bundles](#3-starter-packs--iap-bundles)
   - [Subscription / VIP](#4-subscription--vip)
   - [Season Pass (Catastrophe Cycle)](#5-season-pass-catastrophe-cycle)
   - [Cosmetics](#6-cosmetics)
   - [Card Payments (APK distribution)](#7-card-payments-apk-distribution)
   - [Google Play Billing (Play Store)](#8-google-play-billing-play-store)
   - [Crypto Payments (APK build only)](#9-crypto-payments-apk-build-only)
3. [Staged Rollout](#staged-rollout)
4. [Integration Architecture](#integration-architecture)
5. [Guardrails](#guardrails)

---

## Design Philosophy

> **Maximise revenue without discouraging play.**

The two failure modes we avoid:

- **Under-monetising**: leaving money on the table by not presenting value to willing payers.
- **Over-monetising**: spamming ads, paywalling core content, or breaking balance — which kills retention and reviews.

The sweet spot is a game that is **genuinely fun for free**, with **optional, fair, clearly-priced** upgrades for players who want more convenience or personalisation. Every paying player was a happy free player first.

---

## Revenue Avenues

### 1. Rewarded Ads (optional)

**What**: Short (≤30 s) opt-in video ads that grant a one-time bonus.

**Triggers** (player-initiated only):
- "Double this idle-collection window" (2×, up to the current offline cap)
- "Instant-finish one small task" (tasks under a configurable time threshold)
- "Extra expedition slot for 1 hour"
- "Streak shield — protect today's login streak"

**Implementation**: `AdProvider` interface; `NoOpAdProvider` in MVP; wired to a real network SDK (e.g. AdMob, Unity Ads, IronSource) in Phase 2.

**Key rule**: Ads are *never* shown without player initiation. No interstitials on launch, level-up, or app resume.

---

### 2. One-Time "Remove Ads" Purchase

**What**: A single IAP that permanently disables all ad prompts for this account.

**Replacement**: Instead of ad prompts, the player gets a small daily freebie (e.g. +10 % idle bonus for 2 hours) as a thank-you.

**Why it works**: Players who hate ads are already not clicking them; this converts their frustration into a purchase and improves their experience.

**Implementation**: `IapProvider.purchaseRemoveAds()` — no-op in MVP; wired to Google Play Billing / Stripe in Phase 2.

---

### 3. Starter Packs & IAP Bundles

**Starter Pack** (one-time, deep-discounted):
- A small resource bundle + cosmetic to kickstart the early game.
- Only offered once, in the first 48 hours of play.
- Priced to feel like an impulse buy (~$1.99–$2.99).

**Resource / Logistics Pack** (convenience):
- Speeds up mid-game production chains.
- No permanent power advantage — equivalent to ~4–8 hours of offline progress.

**Cosmetic Packs**:
- Visual themes, building skins, UI colour palettes.
- Purely cosmetic; no gameplay effect.

**Implementation**: `IapProvider.queryProducts()` / `IapProvider.launchPurchaseFlow()` — no-op in MVP.

---

### 4. Subscription / VIP

**Name**: *Patron of CIVILTAS* (or *VIP Tier*)

**Benefits** (convenience + cosmetics, never game-breaking):
- Small daily premium-currency stipend (~25–50 units/day)
- Higher offline-progress cap (+50 % cap, not +50 % production rate)
- Extra build/research queue slots (1 additional slot)
- Exclusive cosmetic theme per subscription tier
- Early access to new story chapters / lore drops

**Price target**: ~$3.99–$4.99/month

**What it explicitly does NOT include**:
- Permanent production multipliers that outpace free players
- Exclusive gameplay content that blocks story progression
- Anything that makes free players feel cheated

**Implementation**: `IapProvider.querySubscriptions()` / `IapProvider.launchSubscriptionFlow()` — no-op in MVP; wired in Phase 2 after Play Store setup.

---

### 5. Season Pass (Catastrophe Cycle)

Each *Catastrophe* (a game-world event that resets/shifts certain systems) is treated as a **season**.

**Free Track** (everyone gets this):
- Seasonal objectives, cosmetic milestones (earned through play).
- Story chapter for the catastrophe event.

**Paid Track** (~$4.99/season):
- Premium cosmetics (building skins, relic visuals, UI frame).
- Story chapter *earlier* (not exclusive — free players get it at season end).
- Convenience boosts during the catastrophe window (not after).
- Exclusive animated "Survivor" title for that season.

**Key rule**: Free players can complete the season. Paid track accelerates and cosmetically enriches but does not lock the story.

**Implementation**: `IapProvider.purchaseSeasonPass(seasonId)` — no-op in MVP; uses same IAP interface as bundles.

---

### 6. Cosmetics

**Scope**:
- Building visual skins (all tiers)
- Base / city background themes
- UI colour palettes and icon sets
- Special "relic" particle effects
- Player title/badge (season pass remnants, rare achievements)

**Pricing**: individual ~$0.99–$2.99; bundles ~$4.99–$7.99.

**Rule**: Cosmetics are purely visual. No stat attached.

**Implementation**: handled through `IapProvider` product catalogue.

---

### 7. Card Payments (APK Distribution)

**Applies to**: Direct APK sideload builds distributed on free app sites and the project website.

**Provider**: Stripe (or equivalent PCI-compliant processor).

**Scope**: All digital goods that would otherwise use Google Play Billing.

**Notes**:
- Must not be included in the Google Play Store build (Play policy prohibits alternative payment systems for digital goods).
- Controlled via a build-time feature flag: `PAYMENT_CHANNEL=stripe` vs `PAYMENT_CHANNEL=play`.
- Implementation uses `PaymentProvider` interface so the same checkout flow works regardless of provider.

---

### 8. Google Play Billing (Play Store)

**Applies to**: The official Google Play Store build.

**Requirement**: All digital goods (IAP, subscriptions, season passes) *must* go through Google Play Billing. No exceptions.

**Implementation**: A `GooglePlayPaymentProvider` that implements `PaymentProvider` and `IapProvider`, wired in when `PAYMENT_CHANNEL=play`.

**Notes**:
- Play Billing requires a specific SDK (`com.android.billingclient`).
- Subscription management (cancel, refund) is handled through the Play Store UI.
- All price points must be set in the Play Console.

---

### 9. Crypto Payments (APK Build Only)

**Applies to**: Direct APK builds *only* — never the Play Store build.

**Accepted currencies** (target): BTC, ETH, SOL (extendable via `CryptoPaymentAdapter`).

**Compliance requirements** (before implementation):
- KYC/AML requirements depend on jurisdiction and amounts; review before launch.
- For small cosmetic purchases, a non-custodial approach (user signs a transaction to their own wallet) keeps compliance simpler.
- Do not store user private keys in the app under any circumstances.

**Architecture**:
- `PaymentProvider` interface with a `CryptoPaymentProvider` implementation.
- `WalletConnectAdapter` for connecting external wallets (WalletConnect v2).
- Watch-only wallet support (user pastes public address for transaction verification).

**Implementation**: Phase 3; interface stubs present from MVP.

---

## Staged Rollout

| Phase | Version | Monetization Active |
|-------|---------|----------------------|
| **MVP** | v0.1 | Interfaces only (no-op); no money flows |
| **Phase 2a** | v0.2 | Rewarded ads (AdMob/Unity), Remove Ads IAP, Starter Pack |
| **Phase 2b** | v0.3 | Subscription/VIP, Season Pass, Cosmetic packs, full IAP catalogue |
| **Phase 2c** | v0.4 | Cloud save (opt-in); account-linked purchases |
| **Phase 2d** | v0.5 | Google Play Store release; Play Billing replaces stubs in Play build |
| **Phase 3** | v1.0+ | Crypto payments (APK build); WalletConnect; Stripe card payments |

---

## Integration Architecture

All monetization is accessed through three interfaces defined in `app/src/main/java/com/civiltas/app/monetization/`:

```
MonetizationProvider   — top-level façade (resolves AdProvider, IapProvider, PaymentProvider)
AdProvider             — rewarded ad lifecycle (load, show, callbacks)
IapProvider            — in-app purchases and subscriptions (query, purchase, restore)
PaymentProvider        — external payment processing (card, crypto)
NoOpMonetizationProvider — default implementation; all methods are no-ops or return "unavailable"
```

Swapping providers (e.g. from `NoOp` to `GooglePlayIapProvider`) requires only a change to the dependency injection binding — not a change to any game logic.

See source stubs in [`app/src/main/java/com/civiltas/app/monetization/`](../app/src/main/java/com/civiltas/app/monetization/).

---

## Guardrails

These rules are **non-negotiable**. Any feature that violates them must be redesigned before shipping.

### G-1 — No Forced Ads
Ads are shown **only** when the player explicitly taps an ad-grant button. No ads on:
- App launch or resume
- Level-up screens
- Building completion
- Any automatic trigger

### G-2 — No Pay-to-Win Power Spikes
Paid items must not create a permanent, compounding advantage that makes the free game feel broken. Specifically:
- No purchased production multiplier > 1.5× baseline for any resource
- No purchased item that blocks story content from free players permanently
- Subscription and season-pass boosts apply to **convenience** (caps, queues, cosmetics), not raw power

### G-3 — Keep Gameplay Fun First
If a monetization mechanism is added and playtesting reveals it makes the game feel unfair, grindy, or pay-walled, it is removed or redesigned regardless of short-term revenue impact. Long-term retention > short-term conversion.

### G-4 — Full Transparency
- All prices displayed in the player's local currency before purchase
- No fake countdown timers on offers
- No confusing premium-currency obfuscation (if premium currency is added, the real-money cost per unit must always be visible)
- Loot boxes (if added) must disclose all item odds

### G-5 — Offline Always Works
No monetization mechanism may block, degrade, or interrupt offline play. Purchasing, ad-watching, and syncing are **additive extras** — the core game runs entirely without a network connection.

### G-6 — No Private Keys in App
The wallet/crypto feature will never store, generate, or transmit user private keys. The app supports connect-external-wallet and watch-only modes only.
