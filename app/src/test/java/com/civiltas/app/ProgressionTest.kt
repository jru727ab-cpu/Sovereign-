package com.civiltas.app

import com.civiltas.app.domain.model.xpRequiredForLevel
import org.junit.Assert.*
import org.junit.Test

class ProgressionTest {

    @Test
    fun `xp required increases with level`() {
        val level1 = xpRequiredForLevel(1)
        val level2 = xpRequiredForLevel(2)
        val level10 = xpRequiredForLevel(10)
        assertTrue("Level 2 should require more XP than level 1", level2 > level1)
        assertTrue("Level 10 should require more XP than level 2", level10 > level2)
    }

    @Test
    fun `level 1 requires 100 xp`() {
        assertEquals(100L, xpRequiredForLevel(1))
    }
}
