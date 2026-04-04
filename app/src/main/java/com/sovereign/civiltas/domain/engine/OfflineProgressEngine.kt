package com.sovereign.civiltas.domain.engine

import com.sovereign.civiltas.domain.model.GameState
import kotlin.math.min

object OfflineProgressEngine {

    fun computeOfflineGains(state: GameState, nowEpoch: Long): OfflineGains {
        val elapsedMs = nowEpoch - state.lastSeenEpoch
        if (elapsedMs <= 0L) return OfflineGains.ZERO

        val elapsedSec = (elapsedMs / 1000.0).coerceAtLeast(0.0)
        val cappedSec = min(elapsedSec, state.offlineCap)

        val oreGained = state.orePerSecond * cappedSec
        val stoneGained = state.stonePerSecond * cappedSec
        val knowledgeGained = state.knowledgePerSecond * cappedSec
        val xpGained = (cappedSec * 0.5).toLong()

        return OfflineGains(
            elapsedSeconds = cappedSec,
            oreGained = oreGained,
            stoneGained = stoneGained,
            knowledgeGained = knowledgeGained,
            xpGained = xpGained
        )
    }

    fun applyGains(state: GameState, gains: OfflineGains, nowEpoch: Long): GameState {
        return state.copy(
            ore = state.ore + gains.oreGained,
            stone = state.stone + gains.stoneGained,
            knowledge = state.knowledge + gains.knowledgeGained,
            xp = state.xp + gains.xpGained,
            lastSeenEpoch = nowEpoch
        )
    }
}

data class OfflineGains(
    val elapsedSeconds: Double,
    val oreGained: Double,
    val stoneGained: Double,
    val knowledgeGained: Double,
    val xpGained: Long
) {
    companion object {
        val ZERO = OfflineGains(0.0, 0.0, 0.0, 0.0, 0L)
    }
}
