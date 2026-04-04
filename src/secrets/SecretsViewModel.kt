package com.civiltas.secrets

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ---------------------------------------------------------------------------
// Unlock state for a single secret
// ---------------------------------------------------------------------------

enum class UnlockStatus { LOCKED, PENDING_UNLOCK, UNLOCKED }

data class SecretState(
    val secret: Secret,
    val status: UnlockStatus = UnlockStatus.LOCKED,
    val pendingUnlockAtEpoch: Long? = null  // epoch millis when unlock completes
)

// ---------------------------------------------------------------------------
// Player state snapshot passed into the ViewModel
// ---------------------------------------------------------------------------

data class PlayerProgress(
    val completedQuests: Set<String> = emptySet(),
    val completedDailyTasks: Int = 0,
    val completedExpeditions: Set<String> = emptySet(),
    val completedLongExpeditions: Set<String> = emptySet(),
    val completedResearchMilestones: Set<String> = emptySet(),
    val seasonPassFree: Boolean = false,
    val seasonPassPremium: Boolean = false,
    val completedCatastropheCycles: Int = 0
)

// ---------------------------------------------------------------------------
// Catastrophe forecast stage (0–4)
// ---------------------------------------------------------------------------

enum class ForecastStage(val label: String) {
    SILENCE("Silence"),
    TREMORS("Tremors"),
    SIGNS("Signs"),
    WARNINGS("Warnings"),
    IMMINENT("Imminent")
}

// ---------------------------------------------------------------------------
// SecretsViewModel — drive this from your Compose screen
// ---------------------------------------------------------------------------
// NOTE: In an Android project this would extend androidx.lifecycle.ViewModel.
// It is kept as a plain class here so the src/ stubs compile without the
// android.jar on the classpath during offline development / unit tests.
// ---------------------------------------------------------------------------

class SecretsViewModel(
    private val billing: BillingProvider = StubBillingProvider(),
    private val clock: () -> Long = System::currentTimeMillis
) {

    // Maximum secrets a player may unlock per calendar day (pacing guardrail)
    private val dailyUnlockLimit = 3

    private val _secretStates = MutableStateFlow(
        SecretsData.all.map { SecretState(secret = it) }
    )
    val secretStates: StateFlow<List<SecretState>> = _secretStates.asStateFlow()

    private val _forecastStage = MutableStateFlow(ForecastStage.SILENCE)
    val forecastStage: StateFlow<ForecastStage> = _forecastStage.asStateFlow()

    // Track daily unlock pacing: store the calendar date string ("yyyy-MM-dd") of the last reset.
    private var lastResetDate: String = todayDateString()
    private var unlockedTodayCount = 0

    /** Returns today's date as a simple "yyyy-MM-dd" string for day-boundary detection. */
    private fun todayDateString(): String {
        val cal = java.util.Calendar.getInstance()
        val year  = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH) + 1          // MONTH is 0-based
        val day   = cal.get(java.util.Calendar.DAY_OF_MONTH)
        return "$year-${month.toString().padStart(2,'0')}-${day.toString().padStart(2,'0')}"
    }

    /** Reset daily counter if the calendar day has changed since the last call. */
    private fun maybeResetDailyCount() {
        val today = todayDateString()
        if (today != lastResetDate) {
            unlockedTodayCount = 0
            lastResetDate = today
        }
    }

    // ------------------------------------------------------------------
    // Evaluate which secrets a player has earned (call on login / resume)
    // ------------------------------------------------------------------

    fun evaluateEarnedSecrets(progress: PlayerProgress) {
        val updated = _secretStates.value.map { state ->
            if (state.status != UnlockStatus.LOCKED) return@map state

            val earned = when (state.secret.earnPath) {
                EarnPath.DAILY_TASK ->
                    progress.completedDailyTasks > 0

                EarnPath.QUEST ->
                    progress.completedQuests.isNotEmpty()

                EarnPath.EXPEDITION ->
                    progress.completedExpeditions.isNotEmpty()

                EarnPath.LONG_EXPEDITION ->
                    progress.completedLongExpeditions.isNotEmpty()

                EarnPath.RESEARCH_MILESTONE ->
                    progress.completedResearchMilestones.isNotEmpty()

                EarnPath.SEASON_PASS_FREE ->
                    progress.seasonPassFree

                EarnPath.SEASON_PASS_PREMIUM ->
                    progress.seasonPassPremium

                EarnPath.CATASTROPHE_CYCLE_COMPLETION ->
                    progress.completedCatastropheCycles > 0
            }

            if (earned) beginUnlock(state) else state
        }
        _secretStates.value = updated
    }

    // ------------------------------------------------------------------
    // Begin unlock: apply delay if required; enforce daily limit
    // ------------------------------------------------------------------

    private fun beginUnlock(state: SecretState): SecretState {
        maybeResetDailyCount()
        if (unlockedTodayCount >= dailyUnlockLimit) return state  // pacing limit

        val delayMs = state.secret.unlockDelayHours * 3_600_000L
        return if (delayMs == 0L) {
            unlockedTodayCount++
            state.copy(status = UnlockStatus.UNLOCKED)
        } else {
            state.copy(
                status = UnlockStatus.PENDING_UNLOCK,
                pendingUnlockAtEpoch = clock() + delayMs
            )
        }
    }

    // ------------------------------------------------------------------
    // Tick: promote PENDING_UNLOCK secrets whose timer has elapsed
    // ------------------------------------------------------------------

    fun tick() {
        maybeResetDailyCount()
        val now = clock()
        _secretStates.value = _secretStates.value.map { state ->
            if (state.status == UnlockStatus.PENDING_UNLOCK &&
                state.pendingUnlockAtEpoch != null &&
                now >= state.pendingUnlockAtEpoch
            ) {
                // Count this delayed unlock against the daily limit when it actually resolves.
                unlockedTodayCount++
                state.copy(status = UnlockStatus.UNLOCKED, pendingUnlockAtEpoch = null)
            } else {
                state
            }
        }
    }

    // ------------------------------------------------------------------
    // Purchase: route through BillingProvider; on success unlock immediately
    // Guardrail: only purchasable secrets with a non-NONE purchaseType
    // ------------------------------------------------------------------

    suspend fun purchase(secretId: String): PurchaseResult {
        val state = _secretStates.value.firstOrNull { it.secret.id == secretId }
            ?: return PurchaseResult.Failure("Secret not found")

        if (!state.secret.purchasable || state.secret.purchaseType == PurchaseType.NONE) {
            return PurchaseResult.Failure("This secret cannot be purchased")
        }

        if (state.status == UnlockStatus.UNLOCKED) {
            return PurchaseResult.AlreadyOwned
        }

        val result = billing.purchase(
            sku = state.secret.id,
            displayName = state.secret.title,
            purchaseType = state.secret.purchaseType
        )

        if (result is BillingResult.Success) {
            _secretStates.value = _secretStates.value.map {
                if (it.secret.id == secretId) it.copy(
                    status = UnlockStatus.UNLOCKED,
                    pendingUnlockAtEpoch = null
                ) else it
            }
            return PurchaseResult.Success
        }

        return PurchaseResult.Failure((result as? BillingResult.Failure)?.reason ?: "Purchase failed")
    }

    // ------------------------------------------------------------------
    // Update the catastrophe forecast
    // ------------------------------------------------------------------

    fun updateForecast(stage: ForecastStage) {
        _forecastStage.value = stage
    }

    // ------------------------------------------------------------------
    // Convenience filters for the UI
    // ------------------------------------------------------------------

    fun filterByCategory(category: SecretCategory): List<SecretState> =
        _secretStates.value.filter { it.secret.category == category }

    fun filterByTier(tier: SecretTier): List<SecretState> =
        _secretStates.value.filter { it.secret.tier == tier }

    fun unlockedSecrets(): List<SecretState> =
        _secretStates.value.filter { it.status == UnlockStatus.UNLOCKED }
}

// ---------------------------------------------------------------------------
// Purchase result
// ---------------------------------------------------------------------------

sealed class PurchaseResult {
    object Success : PurchaseResult()
    object AlreadyOwned : PurchaseResult()
    data class Failure(val reason: String) : PurchaseResult()
}
