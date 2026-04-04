package com.civiltas.app

/**
 * GameEngine — pure-Kotlin offline idle engine.
 *
 * No Android imports: fully unit-testable without Robolectric.
 * Responsibilities:
 *  - compute offline progress from elapsed time
 *  - award XP, handle level-up
 *  - persist / restore state via StorageBackend interface
 *
 * Deliberately NOT responsible for: UI, payments, network, analytics.
 */
class GameEngine(private val storage: StorageBackend) {

    private var _state = GameState()
    val state: GameState get() = _state

    // ── Persistence keys ──────────────────────────────────────────────────────

    private object Keys {
        const val IRON = "iron"
        const val GOLD = "gold"
        const val OIL = "oil"
        const val XP = "xp"
        const val LEVEL = "level"
        const val LAST_SAVE = "last_save"
        const val CATASTROPHE_CD = "catastrophe_cd"
    }

    // ── Public API ─────────────────────────────────────────────────────────────

    /** Load saved state and apply offline progress since last session. */
    fun loadAndApplyOfflineProgress(nowEpochSec: Long = epochNow()) {
        val saved = loadState()
        val elapsedSec = (nowEpochSec - saved.lastSaveEpoch).coerceAtLeast(0L)
        _state = applyTick(saved, elapsedSec)
    }

    /** Advance the engine by [deltaSec] seconds (called from UI coroutine tick). */
    fun tick(deltaSec: Long) {
        _state = applyTick(_state, deltaSec)
    }

    /** Persist current state. Call from onPause / periodic save. */
    fun save(nowEpochSec: Long = epochNow()) {
        val s = _state.copy(lastSaveEpoch = nowEpochSec)
        _state = s
        storage.saveFloat(Keys.IRON, s.iron)
        storage.saveFloat(Keys.GOLD, s.gold)
        storage.saveFloat(Keys.OIL, s.oil)
        storage.saveInt(Keys.XP, s.xp)
        storage.saveInt(Keys.LEVEL, s.level)
        storage.saveLong(Keys.LAST_SAVE, s.lastSaveEpoch)
        storage.saveLong(Keys.CATASTROPHE_CD, s.catastropheCountdownSec)
    }

    // ── Internal helpers ───────────────────────────────────────────────────────

    private fun loadState(): GameState = GameState(
        iron = storage.loadFloat(Keys.IRON, 0f),
        gold = storage.loadFloat(Keys.GOLD, 0f),
        oil = storage.loadFloat(Keys.OIL, 0f),
        xp = storage.loadInt(Keys.XP, 0),
        level = storage.loadInt(Keys.LEVEL, 1),
        lastSaveEpoch = storage.loadLong(Keys.LAST_SAVE, epochNow()),
        catastropheCountdownSec = storage.loadLong(
            Keys.CATASTROPHE_CD, GameState.CATASTROPHE_CYCLE_SEC
        )
    )

    private fun applyTick(s: GameState, elapsedSec: Long): GameState {
        if (elapsedSec <= 0L) return s

        // Cap offline progress at 8 hours to avoid extreme catch-up exploits.
        val cappedSec = elapsedSec.coerceAtMost(8L * 3600)

        val newIron = s.iron + Resource.IRON.ratePerSecond * cappedSec * levelMultiplier(s.level)
        val newGold = s.gold + Resource.GOLD.ratePerSecond * cappedSec * levelMultiplier(s.level)
        val newOil  = s.oil  + Resource.OIL.ratePerSecond  * cappedSec * levelMultiplier(s.level)

        // 1 XP per 10 seconds of accumulation
        val earnedXp = (cappedSec / 10).toInt()
        val totalXp = s.xp + earnedXp

        // Level-up (single level at a time for simplicity)
        val newLevel = computeLevel(totalXp, s.level)
        val remainingXp = totalXp % (newLevel * GameState.XP_PER_LEVEL)

        // Catastrophe countdown decrements with real time (not capped)
        val newCd = (s.catastropheCountdownSec - elapsedSec).coerceAtLeast(0L)

        return s.copy(
            iron = newIron,
            gold = newGold,
            oil = newOil,
            xp = remainingXp,
            level = newLevel,
            catastropheCountdownSec = newCd
        )
    }

    /** Production multiplier scales linearly with level — simple and balanced. */
    private fun levelMultiplier(level: Int): Float = 1f + (level - 1) * 0.1f

    private fun computeLevel(totalXp: Int, currentLevel: Int): Int {
        val threshold = currentLevel * GameState.XP_PER_LEVEL
        return if (totalXp >= threshold) currentLevel + 1 else currentLevel
    }

    private fun epochNow() = System.currentTimeMillis() / 1000L
}
