package com.sovereign.civiltas

import com.sovereign.civiltas.domain.engine.ResourceEngine
import com.sovereign.civiltas.domain.model.GameState
import org.junit.Assert.*
import org.junit.Test

class ResourceEngineTest {

    @Test
    fun `base rates correct at level 0`() {
        val state = GameState()
        val rates = ResourceEngine.computeRates(state)
        assertEquals(0.1, rates.orePerSecond, 0.001)
        assertEquals(0.05, rates.stonePerSecond, 0.001)
    }

    @Test
    fun `upgrade level increases rate`() {
        val state = GameState(oreUpgradeLevel = 2)
        val rates = ResourceEngine.computeRates(state)
        assertTrue(rates.orePerSecond > 0.1)
    }

    @Test
    fun `xp for level is increasing`() {
        val xp1 = ResourceEngine.xpForLevel(1)
        val xp2 = ResourceEngine.xpForLevel(2)
        val xp5 = ResourceEngine.xpForLevel(5)
        assertTrue(xp2 > xp1)
        assertTrue(xp5 > xp2)
    }

    @Test
    fun `level from xp correct`() {
        val level1 = ResourceEngine.levelFromXp(0L)
        assertEquals(1, level1)
        val level2 = ResourceEngine.levelFromXp(ResourceEngine.xpForLevel(2))
        assertEquals(2, level2)
    }
}
