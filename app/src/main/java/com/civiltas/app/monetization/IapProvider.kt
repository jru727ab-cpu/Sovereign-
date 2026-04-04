package com.civiltas.app.monetization

/**
 * In-app purchase and subscription provider interface.
 *
 * Covers all digital-goods flows:
 * - One-time purchases (remove ads, starter pack, IAP bundles, cosmetics, season pass)
 * - Subscriptions (VIP / Patron)
 *
 * Distribution channel rules:
 * - Google Play Store build  → GooglePlayIapProvider (uses Play Billing library).
 * - Direct APK build         → StripeIapProvider (uses Stripe or equivalent).
 *
 * Phase 2: wire up the appropriate concrete implementation.
 * See docs/MONETIZATION.md for the full product catalogue.
 */
interface IapProvider {

    // -------------------------------------------------------------------------
    // Product catalogue
    // -------------------------------------------------------------------------

    /**
     * Fetch available one-time products from the billing provider.
     * Returns an empty list in the no-op implementation.
     */
    suspend fun queryProducts(): List<ProductInfo>

    /**
     * Fetch available subscription products from the billing provider.
     * Returns an empty list in the no-op implementation.
     */
    suspend fun querySubscriptions(): List<ProductInfo>

    // -------------------------------------------------------------------------
    // Purchase flows
    // -------------------------------------------------------------------------

    /**
     * Launch the purchase flow for the "Remove Ads" one-time product.
     * Returns [PurchaseResult.NOT_AVAILABLE] in the no-op implementation.
     */
    suspend fun purchaseRemoveAds(): PurchaseResult

    /**
     * Launch the purchase flow for the starter pack.
     * Returns [PurchaseResult.NOT_AVAILABLE] in the no-op implementation.
     */
    suspend fun purchaseStarterPack(): PurchaseResult

    /**
     * Launch the purchase flow for the season pass for [seasonId].
     * Returns [PurchaseResult.NOT_AVAILABLE] in the no-op implementation.
     */
    suspend fun purchaseSeasonPass(seasonId: String): PurchaseResult

    /**
     * Launch the purchase flow for a generic product identified by [productId].
     * Covers IAP bundles and cosmetics.
     * Returns [PurchaseResult.NOT_AVAILABLE] in the no-op implementation.
     */
    suspend fun purchaseProduct(productId: String): PurchaseResult

    /**
     * Launch the subscription flow for the VIP / Patron tier identified by [subscriptionId].
     * Returns [PurchaseResult.NOT_AVAILABLE] in the no-op implementation.
     */
    suspend fun launchSubscriptionFlow(subscriptionId: String): PurchaseResult

    // -------------------------------------------------------------------------
    // Restore / verification
    // -------------------------------------------------------------------------

    /**
     * Restore previously completed purchases (e.g. after reinstall).
     * Returns an empty list in the no-op implementation.
     */
    suspend fun restorePurchases(): List<String>
}

// ─────────────────────────────────────────────────────────────────────────────

/** Lightweight descriptor for a purchasable product returned by the provider. */
data class ProductInfo(
    val productId: String,
    val title: String,
    val description: String,
    /** Localised price string, e.g. "$2.99". */
    val formattedPrice: String,
    val type: ProductType,
)

enum class ProductType {
    ONE_TIME,
    SUBSCRIPTION,
}

/** Outcome of a purchase attempt. */
enum class PurchaseResult {
    /** Purchase completed and verified successfully. */
    SUCCESS,

    /** User cancelled the purchase UI. */
    CANCELLED,

    /** Billing provider is not configured (no-op or missing credentials). */
    NOT_AVAILABLE,

    /** A network or provider-level error occurred. */
    ERROR,

    /** The product was already owned (e.g. Remove Ads already purchased). */
    ALREADY_OWNED,
}
