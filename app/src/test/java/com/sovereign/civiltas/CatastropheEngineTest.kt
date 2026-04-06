package com.sovereign.civiltas

import com.sovereign.civiltas.domain.engine.CatastropheEngine
import com.sovereign.civiltas.domain.model.GameState
import org.junit.Assert.*
import org.junit.Test

class CatastropheEngineTest {

    @Test
    fun `progress advances with time`() {
        val state = GameState(catastropheProgress = 0f)
        val updated = CatastropheEngine.tick(state, 86400.0) // 1 day
        assertTrue(updated.catastropheProgress > 0f)
    }

    @Test
    fun `season resets at full progress`() {
        val state = GameState(catastropheProgress = 0.99f)
        val delta = CatastropheEngine.SEASON_DURATION_SECONDS * 0.05 // push past 1.0
        val updated = CatastropheEngine.tick(state, delta.toDouble())
        assertEquals(0f, updated.catastropheProgress, 0.001f)
    }

    @Test
    fun `threat label correct`() {
        assertEquals("Calm", CatastropheEngine.threatLabel(0.1f))
        assertEquals("Warning", CatastropheEngine.threatLabel(0.6f))
        assertEquals("CRITICAL", CatastropheEngine.threatLabel(0.95f))
    }
}
