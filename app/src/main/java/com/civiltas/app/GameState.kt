package com.civiltas.app

/**
 * GameState — plain data class; no Android dependencies so it is fully unit-testable.
 */
data class GameState(
    val iron: Float = 0f,
    val gold: Float = 0f,
    val oil: Float = 0f,
    val xp: Int = 0,
    val level: Int = 1,
    val lastSaveEpoch: Long = 0L,
    val catastropheCountdownSec: Long = CATASTROPHE_CYCLE_SEC
) {
    val nextLevelXp: Int get() = level * XP_PER_LEVEL

    companion object {
        const val XP_PER_LEVEL = 100
        /** Catastrophe cycle: 7 days in seconds. Display-only for MVP. */
        const val CATASTROPHE_CYCLE_SEC = 7L * 24 * 60 * 60
    }
}

/**
 * Resource enum — add new resource types here; UI and engine pick them up automatically.
 */
enum class Resource(val label: String, val ratePerSecond: Float) {
    IRON("Iron", 0.5f),
    GOLD("Gold", 0.1f),
    OIL("Oil", 0.3f)
}
