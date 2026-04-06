package com.civiltas.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.civiltas.app.data.MiningPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state snapshot for the mining screen.
 *
 * @param totalOre          Total ore collected over the lifetime of this device.
 * @param pendingPassiveOre Ore accrued passively but not yet collected.
 * @param orePerTap         Ore awarded per active tap.
 * @param passiveOrePerHour Baseline passive accrual rate.
 * @param isMining          True while an active-tap animation is playing.
 * @param sessionTaps       Number of taps recorded in the current session.
 * @param tapUpgradeCost    Ore required to unlock the next tap-power tier.
 * @param passiveProgress   0f–1f fraction of a full passive collection cycle.
 * @param lifetimeTaps      Lifetime taps persisted across sessions.
 */
data class MiningUiState(
    val totalOre: Long = 0L,
    val pendingPassiveOre: Long = 0L,
    val orePerTap: Long = MiningPreferences.DEFAULT_ORE_PER_TAP,
    val passiveOrePerHour: Long = MiningPreferences.DEFAULT_PASSIVE_RATE,
    val isMining: Boolean = false,
    val sessionTaps: Int = 0,
    val tapUpgradeCost: Long = 50L,
    val passiveProgress: Float = 0f,
    val lifetimeTaps: Long = 0L,
)

/**
 * ViewModel driving the CIVILTAS mining screen.
 *
 * Responsibilities:
 * - Track active-tap bursts and persist totals.
 * - Accrue passive ore on a coroutine ticker (every second).
 * - Calculate offline ore on cold start by diffing saved timestamps.
 * - Expose an [isMining] flag so the UI can animate the compass spinner.
 */
class MiningViewModel(private val prefs: MiningPreferences) : ViewModel() {

    private val _uiState = MutableStateFlow(MiningUiState())
    val uiState: StateFlow<MiningUiState> = _uiState.asStateFlow()

    /** Controls how many seconds the compass spins fast after a tap. */
    private var miningAnimationJob: Job? = null

    /** Passive tick that fires every [TICK_INTERVAL_MS] ms. */
    private var passiveTickJob: Job? = null

    // ── Passive cycle constants ───────────────────────────────────────────────
    /** Full passive cycle in milliseconds (defaults to 30 seconds). */
    private val passiveCycleMs: Long
        get() = (3_600_000L / _uiState.value.passiveOrePerHour) * PASSIVE_BATCH_SIZE

    /**
     * Ore awarded per completed passive cycle. Kept at 1 for simplicity; the
     * rate is controlled by how often cycles complete.
     */
    private val PASSIVE_BATCH_SIZE = 1L

    init {
        loadState()
        startPassiveTicker()
    }

    // ── Initialisation ────────────────────────────────────────────────────────

    private fun loadState() {
        val savedTotal = prefs.totalOre
        val savedPerTap = prefs.orePerTap
        val savedPassiveRate = prefs.passiveOrePerHour
        val lifetimeTaps = prefs.lifetimeTaps

        // Calculate offline accrual since last save.
        val nowMs = System.currentTimeMillis()
        val elapsedMs = (nowMs - prefs.lastTimestampMillis).coerceAtLeast(0L)
        val capMs = MiningPreferences.PASSIVE_CAP_HOURS * 3_600_000L
        val effectiveMs = elapsedMs.coerceAtMost(capMs)
        val offlineOre = (effectiveMs * savedPassiveRate / 3_600_000L)

        prefs.lastTimestampMillis = nowMs

        _uiState.update { state ->
            state.copy(
                totalOre = savedTotal,
                pendingPassiveOre = offlineOre,
                orePerTap = savedPerTap,
                passiveOrePerHour = savedPassiveRate,
                tapUpgradeCost = nextUpgradeCost(savedPerTap),
                lifetimeTaps = lifetimeTaps,
            )
        }
    }

    // ── Active tapping ────────────────────────────────────────────────────────

    /** Called when the player taps the mine button. */
    fun onTapMine() {
        val gained = _uiState.value.orePerTap
        _uiState.update { state ->
            state.copy(
                totalOre = state.totalOre + gained,
                sessionTaps = state.sessionTaps + 1,
                isMining = true,
                lifetimeTaps = state.lifetimeTaps + 1,
            )
        }
        persistOre()
        prefs.lifetimeTaps = _uiState.value.lifetimeTaps

        // Keep compass spinning fast for MINING_ANIM_DURATION_MS then reset.
        miningAnimationJob?.cancel()
        miningAnimationJob = viewModelScope.launch {
            delay(MINING_ANIM_DURATION_MS)
            _uiState.update { it.copy(isMining = false) }
        }
    }

    // ── Passive collection ────────────────────────────────────────────────────

    /** Called when the player presses the "Collect" button. */
    fun onCollectPassive() {
        val pending = _uiState.value.pendingPassiveOre
        if (pending <= 0L) return
        _uiState.update { state ->
            state.copy(
                totalOre = state.totalOre + pending,
                pendingPassiveOre = 0L,
                passiveProgress = 0f,
            )
        }
        persistOre()
    }

    // ── Upgrade ───────────────────────────────────────────────────────────────

    /** Attempt to purchase the next tap-power upgrade. */
    fun onUpgradeTap() {
        val state = _uiState.value
        if (state.totalOre < state.tapUpgradeCost) return

        val newPerTap = state.orePerTap + 1L
        val newTotal = state.totalOre - state.tapUpgradeCost
        _uiState.update {
            it.copy(
                totalOre = newTotal,
                orePerTap = newPerTap,
                tapUpgradeCost = nextUpgradeCost(newPerTap),
            )
        }
        prefs.orePerTap = newPerTap
        persistOre()
    }

    // ── Passive ticker ────────────────────────────────────────────────────────

    private fun startPassiveTicker() {
        passiveTickJob?.cancel()
        passiveTickJob = viewModelScope.launch {
            var elapsedSinceLastBatch = 0L
            while (true) {
                delay(TICK_INTERVAL_MS)
                elapsedSinceLastBatch += TICK_INTERVAL_MS

                val state = _uiState.value
                val cycleMs = (3_600_000L / state.passiveOrePerHour).coerceAtLeast(1L)

                // Compute 0f–1f progress within current cycle.
                val progress = (elapsedSinceLastBatch.toFloat() / cycleMs.toFloat()).coerceAtMost(1f)

                if (elapsedSinceLastBatch >= cycleMs) {
                    elapsedSinceLastBatch = 0L
                    _uiState.update { s ->
                        s.copy(
                            pendingPassiveOre = s.pendingPassiveOre + PASSIVE_BATCH_SIZE,
                            passiveProgress = 0f,
                        )
                    }
                } else {
                    _uiState.update { s -> s.copy(passiveProgress = progress) }
                }

                prefs.lastTimestampMillis = System.currentTimeMillis()
            }
        }
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    private fun persistOre() {
        prefs.totalOre = _uiState.value.totalOre
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun nextUpgradeCost(currentPerTap: Long): Long =
        50L * currentPerTap * currentPerTap

    override fun onCleared() {
        super.onCleared()
        prefs.lastTimestampMillis = System.currentTimeMillis()
        persistOre()
    }

    companion object {
        /** How long (ms) the compass spins fast after each tap. */
        const val MINING_ANIM_DURATION_MS = 800L

        /** How often the passive ticker fires. */
        const val TICK_INTERVAL_MS = 1000L
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MiningViewModel(com.civiltas.app.data.SharedPrefsMiningPreferences(context)) as T
    }
}
