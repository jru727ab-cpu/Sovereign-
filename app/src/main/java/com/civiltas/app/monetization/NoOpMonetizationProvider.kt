package com.civiltas.app.monetization

/**
 * No-op implementation of all monetization providers used in the MVP.
 *
 * Every method returns a safe "not available" result so game logic can call
 * monetization hooks freely without crashing or requiring a real billing SDK.
 *
 * Replace this with a real implementation (e.g. GooglePlayMonetizationProvider)
 * in Phase 2 by updating the dependency-injection binding — no game-logic
 * changes required.
 */
class NoOpMonetizationProvider : MonetizationProvider {

    override val isAdsRemoved: Boolean = false
    override val isVipActive: Boolean = false

    override fun hasSeasonPass(seasonId: String): Boolean = false

    override val ads: AdProvider = NoOpAdProvider()
    override val iap: IapProvider = NoOpIapProvider()
    override val payments: PaymentProvider = NoOpPaymentProvider()
}

// ─────────────────────────────────────────────────────────────────────────────

private class NoOpAdProvider : AdProvider {

    override val isRewardedAdReady: Boolean = false

    override fun preloadRewardedAd() {
        // No-op: no ad SDK configured in MVP.
    }

    override fun showRewardedAd(
        placement: String,
        onRewarded: () -> Unit,
        onFailed: (AdFailureReason) -> Unit,
    ) {
        onFailed(AdFailureReason.NOT_READY)
    }
}

// ─────────────────────────────────────────────────────────────────────────────

private class NoOpIapProvider : IapProvider {

    override suspend fun queryProducts(): List<ProductInfo> = emptyList()

    override suspend fun querySubscriptions(): List<ProductInfo> = emptyList()

    override suspend fun purchaseRemoveAds(): PurchaseResult = PurchaseResult.NOT_AVAILABLE

    override suspend fun purchaseStarterPack(): PurchaseResult = PurchaseResult.NOT_AVAILABLE

    override suspend fun purchaseSeasonPass(seasonId: String): PurchaseResult =
        PurchaseResult.NOT_AVAILABLE

    override suspend fun purchaseProduct(productId: String): PurchaseResult =
        PurchaseResult.NOT_AVAILABLE

    override suspend fun launchSubscriptionFlow(subscriptionId: String): PurchaseResult =
        PurchaseResult.NOT_AVAILABLE

    override suspend fun restorePurchases(): List<String> = emptyList()
}

// ─────────────────────────────────────────────────────────────────────────────

private class NoOpPaymentProvider : PaymentProvider {

    override val isAvailable: Boolean = false

    override suspend fun initiatePayment(
        productId: String,
        amountCents: Long,
        currency: String,
    ): PaymentResult = PaymentResult.NotAvailable

    override suspend fun checkPaymentStatus(transactionId: String): PaymentStatus =
        PaymentStatus.UNKNOWN
}
