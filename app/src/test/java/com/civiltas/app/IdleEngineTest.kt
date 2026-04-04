package com.civiltas.app

import com.civiltas.app.game.IdleEngine
import org.junit.Assert.*
import org.junit.Test

class IdleEngineTest {

    @Test
    fun `calcOfflineGain returns correct amount for elapsed time`() {
        val opsPerSec = 1.0
        val lastSaveMs = 0L
        val nowMs = 10_000L // 10 seconds
        val gain = IdleEngine.calcOfflineGain(opsPerSec, lastSaveMs, nowMs)
        assertEquals(10.0, gain, 0.001)
    }

    @Test
    fun `calcOfflineGain caps at 8 hours`() {
        val opsPerSec = 1.0
        val lastSaveMs = 0L
        val nowMs = 100 * 3600 * 1000L // 100 hours
        val gain = IdleEngine.calcOfflineGain(opsPerSec, lastSaveMs, nowMs)
        assertEquals(8.0 * 3600, gain, 0.001)
    }

    @Test
    fun `calcOfflineGain returns 0 for same timestamp`() {
        val opsPerSec = 5.0
        val nowMs = 1000L
        val gain = IdleEngine.calcOfflineGain(opsPerSec, nowMs, nowMs)
        assertEquals(0.0, gain, 0.001)
    }

    @Test
    fun `upgradeOPS increases with level`() {
        val base = 0.1
        val upgraded = IdleEngine.upgradeOPS(base, 1)
        assertTrue(upgraded > base)
    }

    @Test
    fun `upgradeCost increases with level`() {
        val cost0 = IdleEngine.upgradeCost(0)
        val cost1 = IdleEngine.upgradeCost(1)
        assertTrue(cost1 > cost0)
        assertEquals(50.0, cost0, 0.001)
    }

    @Test
    fun `manualCollectBonus equals orePerSecond`() {
        val ops = 2.5
        val bonus = IdleEngine.manualCollectBonus(ops)
        assertEquals(ops, bonus, 0.001)
    }
}
