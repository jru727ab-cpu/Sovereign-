package com.civiltas.app

/**
 * StorageBackend — thin interface so MVP (SharedPreferences) can be swapped for
 * Room or a cloud backend later without touching GameEngine.
 */
interface StorageBackend {
    fun loadLong(key: String, default: Long): Long
    fun saveLong(key: String, value: Long)
    fun loadFloat(key: String, default: Float): Float
    fun saveFloat(key: String, value: Float)
    fun loadInt(key: String, default: Int): Int
    fun saveInt(key: String, value: Int)
}

/**
 * PaymentProvider — stub interface for Phase 2 IAP/crypto integration.
 * Implement PlayBillingProvider or CryptoPaymentProvider without touching game logic.
 */
interface PaymentProvider {
    fun isAvailable(): Boolean
    fun launchPurchase(sku: String)
}

/** No-op provider used until a real PaymentProvider is wired in. */
class NoOpPaymentProvider : PaymentProvider {
    override fun isAvailable() = false
    override fun launchPurchase(sku: String) { /* deferred to Phase 2 */ }
}
