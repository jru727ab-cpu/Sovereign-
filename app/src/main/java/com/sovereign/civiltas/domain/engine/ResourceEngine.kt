package com.sovereign.civiltas.domain.engine

import com.sovereign.civiltas.domain.model.GameState
import com.sovereign.civiltas.domain.model.ALL_SKILLS

object ResourceEngine {

    fun computeRates(state: GameState): Rates {
        val unlockedSkills = ALL_SKILLS.filter { it.id in state.unlockedSkillIds }
        var oreMulti = 1.0
        var stoneMulti = 1.0
        var knowledgeMulti = 1.0
        var offlineCap = 3600.0 + (state.energyUpgradeLevel * 1800.0)

        for (skill in unlockedSkills) {
            oreMulti *= skill.effect.oreMultiplier
            stoneMulti *= skill.effect.stoneMultiplier
            knowledgeMulti *= skill.effect.knowledgeMultiplier
            offlineCap += skill.effect.offlineCapBonus
        }

        val baseOre = 0.1 + (state.oreUpgradeLevel * 0.05)
        val baseStone = 0.05 + (state.stoneUpgradeLevel * 0.03)
        val baseKnowledge = 0.01 + (state.gnosisLevel * 0.005)

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
