package com.civiltas.app.monetization

/**
 * External payment provider interface for non-Play-Store distribution.
 *
 * Applies ONLY to direct APK builds (sideload / free app sites / project website).
 * This interface MUST NOT be wired in the Google Play Store build — Play policy
 * requires all digital goods on Play to use Google Play Billing exclusively.
 *
 * Supported providers (Phase 3):
 * - Stripe / card payments  → StripePaymentProvider
 * - Crypto payments         → CryptoPaymentProvider  (WalletConnect v2; non-custodial)
 *
 * Security rule: no implementation of this interface may store, generate, or
 * transmit user private keys. Use connect-external-wallet / watch-only modes only.
 * See docs/MONETIZATION.md § Guardrails G-6.
 */
interface PaymentProvider {

    /**
     * True if this provider is available in the current build and properly
     * configured. Always false in the no-op implementation.
     */
    val isAvailable: Boolean

    /**
     * Initiate a payment for a product with the given [productId] and [amountCents]
     * (in the user's local currency, minor units).
     *
     * @param productId   Stable product identifier (same as [IapProvider] catalogue).
     * @param amountCents Amount in minor units (e.g. 299 for $2.99).
     * @param currency    ISO 4217 currency code (e.g. "USD").
     * @return [PaymentResult] indicating the outcome.
     */
    suspend fun initiatePayment(
        productId: String,
        amountCents: Long,
        currency: String,
    ): PaymentResult

    /**
     * Check the status of a previously initiated payment by [transactionId].
     * Useful for async payment methods (e.g. crypto on-chain confirmation).
     */
    suspend fun checkPaymentStatus(transactionId: String): PaymentStatus
}

// ─────────────────────────────────────────────────────────────────────────────

/** Outcome of a [PaymentProvider.initiatePayment] call. */
sealed class PaymentResult {
    /** Payment completed and confirmed. */
    data class Success(val transactionId: String) : PaymentResult()

    /** User cancelled the payment UI. */
    object Cancelled : PaymentResult()

    /** Provider not configured or not available in this build. */
    object NotAvailable : PaymentResult()

    /** An error occurred; [message] contains a human-readable description. */
    data class Error(val message: String) : PaymentResult()

    /**
     * Payment initiated but awaiting async confirmation (e.g. blockchain confirmation).
     * Poll [PaymentProvider.checkPaymentStatus] with [transactionId].
     */
    data class Pending(val transactionId: String) : PaymentResult()
}

/** Status returned by [PaymentProvider.checkPaymentStatus]. */
enum class PaymentStatus {
    CONFIRMED,
    PENDING,
    FAILED,
    UNKNOWN,
}
