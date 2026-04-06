# CIVILTAS Monetization — Secrets System Scaffolding

## Overview

This document defines the monetization scaffolding for the Secrets system. All payment
processing is **stubbed** in the MVP. Real payment processing must be added per
distribution channel (see §4). The `BillingInterface` abstraction ensures the game logic
never depends on a specific payment provider.

---

## 1. SKU Catalog

All purchasable Secrets are defined in `js/secrets/catalog.js`. Each entry maps a
human-readable Secret to a platform SKU identifier.

### SKU Naming Convention

```
civiltas.<channel>.<type>.<id>
  channel : play | direct | web
  type    : secret | bundle | season_pass | remove_ads | vip
  id      : kebab-case identifier
```

### Current SKUs (MVP)

| SKU | Price (USD) | Contents |
|---|---|---|
| `civiltas.play.secret.lore-first-fracture` | $1.99 | Instant unlock: *The First Fracture* (Lore) |
| `civiltas.play.secret.intel-safe-corridors` | $0.99 | Instant unlock: *The Safe Corridors* (Intel) |
| `civiltas.play.secret.blueprint-foundation-stones` | $1.99 | Instant unlock: *Foundation Stones* (Blueprint) |
| `civiltas.play.bundle.genesis-secrets` | $3.99 | Bundle: all 3 launch Secrets (saves ~33%) |
| `civiltas.play.season_pass.cycle-1` | $4.99/cycle | Premium Season Pass — early Secret access + cosmetics |
| `civiltas.play.remove_ads` | $2.99 | Permanent ad removal |
| `civiltas.play.vip.monthly` | $4.99/mo | Convenience + cosmetics + 1 free Secret/month |

> **Note:** Price points are placeholders. Adjust based on market research and player
> spending data once the game is in soft launch.

---

## 2. BillingInterface (Stub)

The `BillingInterface` (in `js/secrets/billing.js`) is a thin abstraction layer:

```js
class BillingInterface {
  /**
   * Initiate a purchase flow for a SKU.
   * @param {string} skuId
   * @param {function} onSuccess  called with { skuId, transactionId }
   * @param {function} onError    called with { skuId, code, message }
   */
  purchase(skuId, onSuccess, onError) { /* STUB */ }

  /**
   * Restore previously completed purchases (required for iOS/Android).
   * @param {function} onRestored  called with [{ skuId, transactionId }]
   */
  restorePurchases(onRestored) { /* STUB */ }

  /**
   * Query whether a SKU is currently owned.
   * @param {string} skuId
   * @returns {boolean}
   */
  isOwned(skuId) { /* STUB */ }
}
```

Game code always calls `BillingInterface`; it never calls a payment SDK directly. This
means swapping providers (Google Play → Stripe → RevenueCat) only requires changing the
`BillingInterface` implementation, not the game.

---

## 3. Provider Implementation Guide

### 3a. Google Play Billing (required for Play Store distribution)

Google Play requires that **all in-app digital-goods purchases** use the
[Google Play Billing Library](https://developer.android.com/google/play/billing).

Steps when wrapping in a WebView / TWA:
1. Add the `billing` permission to `AndroidManifest.xml`.
2. Add `com.android.billingclient:billing-ktx:<version>` to `build.gradle`.
3. Implement `BillingInterface` using `BillingClient` from the Play Billing Library.
4. Post purchase results to the WebView via `JavascriptInterface`.
5. The WebView's `BillingInterface` receives results and calls `SecretStore.unlock()`.

**Never** process raw card payments inside a Play Store app — this violates Google's
Developer Distribution Agreement.

### 3b. Direct APK / Sideload Distribution

For builds distributed outside the Play Store (GitHub Releases, itch.io, F-Droid-style):
- **Cards**: Stripe Payment Links or a Stripe Checkout WebView (Stripe SDK for Android or
  a redirect to a hosted checkout page).
- **Crypto**: Direct on-chain payment verification via a lightweight backend or use a
  service like Coinbase Commerce (WebView redirect + webhook).
- No Play Billing dependency for direct builds.

### 3c. Web Distribution (PWA / Browser)

- Use Stripe.js (loaded client-side) or a backend checkout endpoint.
- Unlock is confirmed server-side; the client polls `/api/secrets/owned` and calls
  `SecretStore.unlock()` on confirmation.

---

## 4. Restore & Receipt Validation

**MVP stub**: Purchases are persisted to `localStorage` only. This is fine for testing.

**Production requirement**: All purchases must be verified server-side (or via platform
SDK receipt validation) before `SecretStore.unlock()` is called. Receipt validation
prevents players from unlocking paid Secrets by modifying `localStorage`.

```
Flow (production):
  Player taps "Buy" → BillingInterface.purchase()
    → Platform SDK processes payment
    → Platform SDK returns signed receipt
    → POST /api/verify-receipt { receipt, skuId }
    → Server validates receipt with platform API
    → Server responds { valid: true, secretId }
    → Client calls SecretStore.unlock(secretId)
```

---

## 5. Fair Monetization Checklist

- [x] Every Secret has at least one free earnable path.
- [x] Purchases only accelerate timing — never lock out core gameplay.
- [x] Bundle discount ≥ 25% vs individual purchase sum.
- [x] Season pass content is the same as free track content, just earlier.
- [x] VIP subscription provides convenience, not insurmountable power.
- [x] No loot boxes or randomised paid content for Secrets.
- [x] "Remove Ads" is a one-time permanent purchase (no forced ads; only optional rewarded).
- [x] Full narrative ending is earnable without spending money.

---

## 6. Future Monetization Hooks

- **Cosmetic Secret Card Frames** (Season Pass premium reward) — no gameplay effect.
- **Gnosis XP Boosters** (temporary, time-limited, purchasable once per cycle) — not stacking.
- **Name in the Archive** — cosmetic: your player name appears in an in-game lore scroll.
- **Patron's Edition** — one-time large bundle for superfans (all current + future Secrets, cosmetics, VIP forever).
