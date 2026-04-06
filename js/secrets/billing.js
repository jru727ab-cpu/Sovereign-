/**
 * CIVILTAS — Secrets System: BillingInterface (STUB)
 * ====================================================
 * Abstraction layer between the game and any payment provider.
 *
 * How to implement a real provider:
 * 1. Subclass BillingInterface (or replace this module's default export).
 * 2. Implement purchase(), restorePurchases(), and isOwned() using the
 *    provider's SDK (Google Play Billing, Stripe, etc.).
 * 3. On successful purchase: call SecretStore.unlock(secretId) AFTER
 *    server-side receipt validation (see docs/monetization.md §4).
 *
 * Google Play Billing requirement:
 *   All in-app digital goods sold through the Play Store MUST use the
 *   Google Play Billing Library. Raw card / crypto payments inside a
 *   Play Store app violate the Developer Distribution Agreement.
 *   See: https://developer.android.com/google/play/billing
 *
 * Direct APK / Web builds:
 *   May use Stripe, Coinbase Commerce, or another processor.
 *   Keep a "channel" flag (PLAY | DIRECT | WEB) to select the correct
 *   BillingInterface implementation at runtime.
 */

'use strict';

/**
 * @enum {string}
 */
const BillingChannel = Object.freeze({
  PLAY:   'PLAY',   // Google Play Billing (required for Play Store)
  DIRECT: 'DIRECT', // Direct APK / sideload (Stripe, crypto, etc.)
  WEB:    'WEB',    // Browser / PWA (Stripe.js, server checkout)
  STUB:   'STUB',   // Development stub — no real payments
});

/**
 * @typedef {Object} PurchaseResult
 * @property {string} skuId
 * @property {string} transactionId
 */

/**
 * @typedef {Object} PurchaseError
 * @property {string} skuId
 * @property {string} code    - Provider-specific error code
 * @property {string} message - Human-readable description
 */

class BillingInterface {
  /**
   * @param {string} channel - One of BillingChannel
   */
  constructor(channel = BillingChannel.STUB) {
    this.channel = channel;
  }

  /**
   * Initiate a purchase flow for the given SKU.
   *
   * STUB behaviour: logs to console and calls onError with a "not implemented" message.
   * Replace with real provider logic.
   *
   * @param {string}   skuId
   * @param {function(PurchaseResult): void} onSuccess
   * @param {function(PurchaseError): void}  onError
   */
  purchase(skuId, onSuccess, onError) {
    console.warn(`[BillingInterface:${this.channel}] purchase() is a stub. SKU: ${skuId}`);
    onError({
      skuId,
      code:    'BILLING_NOT_IMPLEMENTED',
      message: 'Payment processing is not yet configured. Please try again later.',
    });
  }

  /**
   * Restore previously completed purchases.
   * Required for iOS, recommended for Android on re-install.
   *
   * STUB behaviour: calls onRestored with an empty array.
   *
   * @param {function(PurchaseResult[]): void} onRestored
   */
  restorePurchases(onRestored) {
    console.warn(`[BillingInterface:${this.channel}] restorePurchases() is a stub.`);
    onRestored([]);
  }

  /**
   * Query whether a SKU is currently owned (locally cached entitlement).
   * In production this should reflect the platform's entitlement check.
   *
   * STUB behaviour: always returns false.
   *
   * @param {string} skuId
   * @returns {boolean}
   */
  isOwned(skuId) {
    return false;
  }
}

// ---------------------------------------------------------------------------
// Singleton — the rest of the app imports this instance.
// Replace with a real implementation once a payment provider is chosen.
// ---------------------------------------------------------------------------
const billingInterface = new BillingInterface(BillingChannel.STUB);

export { BillingInterface, BillingChannel, billingInterface };
