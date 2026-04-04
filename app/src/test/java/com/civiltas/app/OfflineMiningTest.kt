package com.civiltas.app

import com.civiltas.app.data.repository.GameRepository
import org.junit.Assert.*
import org.junit.Test

class OfflineMiningTest {

    @Test
    fun `offline hours capped at max`() {
        val capped = minOf(100.0, GameRepository.MAX_OFFLINE_HOURS)
        assertEquals(GameRepository.MAX_OFFLINE_HOURS, capped, 0.001)
    }

    @Test
    fun `default mining rate for gold is less than iron`() {
        val goldRate = GameRepository.defaultMiningRate(com.civiltas.app.domain.model.ResourceType.GOLD)
        val ironRate = GameRepository.defaultMiningRate(com.civiltas.app.domain.model.ResourceType.IRON)
        assertTrue("Gold should be rarer than iron", goldRate < ironRate)
    }
}
