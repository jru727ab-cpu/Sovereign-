package com.civiltas.secrets

// ---------------------------------------------------------------------------
// BillingProvider interface — swap implementations per distribution channel
// ---------------------------------------------------------------------------
// MVP default: StubBillingProvider (offline-friendly, local receipts)
// Future:       PlayBillingProvider  (Google Play Billing)
//               StripeBillingProvider (direct APK / sideload distribution)
// ---------------------------------------------------------------------------

sealed class BillingResult {
    data class Success(val receiptToken: String) : BillingResult()
    data class Failure(val reason: String) : BillingResult()
    object Cancelled : BillingResult()
}

interface BillingProvider {
    /**
     * Initiate a purchase flow for the given SKU.
     * Implementations must handle their own UI; this function returns
     * only after the flow completes (success, failure, or cancellation).
     *
     * Guardrail: billing is for convenience/cosmetic unlocks only.
     * The caller (SecretsViewModel) validates purchaseType before calling.
     */
    suspend fun purchase(
        sku: String,
        displayName: String,
        purchaseType: PurchaseType
    ): BillingResult

    /**
     * Restore previously completed purchases (e.g., on fresh install).
     * Returns a list of SKU strings for which the player already owns entitlements.
     */
    suspend fun restorePurchases(): List<String>
}

// ---------------------------------------------------------------------------
// Stub implementation — works fully offline; replace for production
// ---------------------------------------------------------------------------

class StubBillingProvider : BillingProvider {

    // In-memory receipt store (replace with SharedPreferences or Room for persistence)
    private val localReceipts = mutableSetOf<String>()

    override suspend fun purchase(
        sku: String,
        displayName: String,
        purchaseType: PurchaseType
    ): BillingResult {
        // Stub: always succeeds. In production, launch the Play Billing / Stripe flow here.
        val token = "stub_receipt_${sku}_${System.currentTimeMillis()}"
        localReceipts.add(sku)
        return BillingResult.Success(receiptToken = token)
    }

    override suspend fun restorePurchases(): List<String> {
        // Stub: returns whatever was purchased in this session (no persistence yet)
        return localReceipts.toList()
    }
}

// ---------------------------------------------------------------------------
// Google Play Billing — stub shell (implement when targeting Play Store)
// ---------------------------------------------------------------------------
// Uncomment and implement when adding the billing:6.x.x dependency.
//
// class PlayBillingProvider(private val activity: Activity) : BillingProvider {
//     override suspend fun purchase(sku: String, displayName: String, purchaseType: PurchaseType): BillingResult {
//         // 1. Launch BillingClient.launchBillingFlow(activity, billingFlowParams)
//         // 2. Await PurchasesUpdatedListener callback via a CompletableDeferred
//         // 3. Acknowledge purchase (consumable or non-consumable as appropriate)
//         // 4. Return BillingResult.Success with the purchase token
//         TODO("Implement Play Billing flow")
//     }
//     override suspend fun restorePurchases(): List<String> {
//         // BillingClient.queryPurchasesAsync(INAPP) + queryPurchasesAsync(SUBS)
//         TODO("Implement Play Billing restore")
//     }
// }
