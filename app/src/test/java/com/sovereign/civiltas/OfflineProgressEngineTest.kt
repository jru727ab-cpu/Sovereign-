package com.sovereign.civiltas

import com.sovereign.civiltas.domain.engine.OfflineGains
import com.sovereign.civiltas.domain.engine.OfflineProgressEngine
import com.sovereign.civiltas.domain.model.GameState
import org.junit.Assert.*
import org.junit.Test

class OfflineProgressEngineTest {

    @Test
    fun `offline gains computed correctly`() {
        val now = System.currentTimeMillis()
        val state = GameState(
            orePerSecond = 1.0,
            stonePerSecond = 0.5,
            knowledgePerSecond = 0.1,
            offlineCap = 3600.0,
            lastSeenEpoch = now - 100_000L // 100 seconds ago
        )
        val gains = OfflineProgressEngine.computeOfflineGains(state, now)
        assertEquals(100.0, gains.oreGained, 0.01)
        assertEquals(50.0, gains.stoneGained, 0.01)
        assertEquals(10.0, gains.knowledgeGained, 0.01)
    }

    @Test
    fun `offline cap is respected`() {
        val now = System.currentTimeMillis()
        val state = GameState(
            orePerSecond = 1.0,
            offlineCap = 60.0,
            lastSeenEpoch = now - 10_000_000L // far in the past
        )
        val gains = OfflineProgressEngine.computeOfflineGains(state, now)
        assertEquals(60.0, gains.oreGained, 0.01)
    }

    @Test
    fun `zero elapsed time returns zero gains`() {
        val now = System.currentTimeMillis()
        val state = GameState(lastSeenEpoch = now)
        val gains = OfflineProgressEngine.computeOfflineGains(state, now)
        assertEquals(OfflineGains.ZERO, gains)
    }
}
