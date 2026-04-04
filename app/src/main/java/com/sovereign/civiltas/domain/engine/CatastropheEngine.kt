package com.sovereign.civiltas.domain.engine

import com.sovereign.civiltas.domain.model.GameState

object CatastropheEngine {
    const val SEASON_DURATION_SECONDS = 7 * 24 * 3600 // 7 days per season

    fun tick(state: GameState, deltaSeconds: Double): GameState {
        val increment = (deltaSeconds / SEASON_DURATION_SECONDS).toFloat()
        val newProgress = (state.catastropheProgress + increment).coerceIn(0f, 1f)
        val newDay = (state.catastropheSeasonDay + (deltaSeconds / 86400).toInt())
            .coerceAtMost(7)

        return if (newProgress >= 1f) {
            // Season rollover - catastrophe event triggered
            state.copy(
                catastropheProgress = 0f,
                catastropheSeasonDay = 0
            )
        } else {
            state.copy(
                catastropheProgress = newProgress,
                catastropheSeasonDay = newDay
            )
        }
    }

    fun threatLabel(progress: Float): String = when {
        progress < 0.25f -> "Calm"
        progress < 0.5f -> "Stirring"
        progress < 0.75f -> "Warning"
        progress < 0.9f -> "Imminent"
        else -> "CRITICAL"
    }

    fun threatColor(progress: Float): Long = when {
        progress < 0.25f -> 0xFF4CAF50L // green
        progress < 0.5f -> 0xFFFFEB3BL // yellow
        progress < 0.75f -> 0xFFFF9800L // orange
        progress < 0.9f -> 0xFFF44336L // red
        else -> 0xFF9C27B0L // purple (critical)
    }
}
