package com.sovereign.civiltas.domain.engine

import com.sovereign.civiltas.domain.model.GameState
import com.sovereign.civiltas.domain.model.ALL_SKILLS

object ResourceEngine {
    private const val BASE_OFFLINE_CAP_SECONDS = 3600.0
    private const val OFFLINE_CAP_PER_ENERGY_LEVEL = 1800.0
    private const val BASE_ORE_RATE = 0.1
    private const val ORE_RATE_PER_LEVEL = 0.05
    private const val BASE_STONE_RATE = 0.05
    private const val STONE_RATE_PER_LEVEL = 0.03
    private const val BASE_KNOWLEDGE_RATE = 0.01
    private const val KNOWLEDGE_RATE_PER_GNOSIS = 0.005

    fun computeRates(state: GameState): Rates {
        val unlockedSkills = ALL_SKILLS.filter { it.id in state.unlockedSkillIds }
        var oreMulti = 1.0
        var stoneMulti = 1.0
        var knowledgeMulti = 1.0
        var offlineCap = BASE_OFFLINE_CAP_SECONDS + (state.energyUpgradeLevel * OFFLINE_CAP_PER_ENERGY_LEVEL)

        for (skill in unlockedSkills) {
            oreMulti *= skill.effect.oreMultiplier
            stoneMulti *= skill.effect.stoneMultiplier
            knowledgeMulti *= skill.effect.knowledgeMultiplier
            offlineCap += skill.effect.offlineCapBonus
        }

        val baseOre = BASE_ORE_RATE + (state.oreUpgradeLevel * ORE_RATE_PER_LEVEL)
        val baseStone = BASE_STONE_RATE + (state.stoneUpgradeLevel * STONE_RATE_PER_LEVEL)
        val baseKnowledge = BASE_KNOWLEDGE_RATE + (state.gnosisLevel * KNOWLEDGE_RATE_PER_GNOSIS)

        return Rates(
            orePerSecond = baseOre * oreMulti,
            stonePerSecond = baseStone * stoneMulti,
            knowledgePerSecond = baseKnowledge * knowledgeMulti,
            offlineCap = offlineCap
        )
    }

    fun xpForLevel(level: Int): Long = (100L * level * level)

    fun levelFromXp(xp: Long): Int {
        var level = 1
        while (xp >= xpForLevel(level + 1)) level++
        return level
    }
}

data class Rates(
    val orePerSecond: Double,
    val stonePerSecond: Double,
    val knowledgePerSecond: Double,
    val offlineCap: Double
)
