package com.sovereign.civiltas.domain.model

enum class SkillTree { MINER, BUILDER, SCHOLAR }

data class Skill(
    val id: String,
    val name: String,
    val description: String,
    val tree: SkillTree,
    val cost: Int, // skill points
    val requires: String? = null, // prerequisite skill id
    val isUnlocked: Boolean = false,
    val effect: SkillEffect
)

data class SkillEffect(
    val oreMultiplier: Double = 1.0,
    val stoneMultiplier: Double = 1.0,
    val knowledgeMultiplier: Double = 1.0,
    val offlineCapBonus: Double = 0.0,
    val xpMultiplier: Double = 1.0
)

val ALL_SKILLS = listOf(
    Skill("miner_1", "Deep Vein Tap", "Ore rate +20%", SkillTree.MINER, 1,
        effect = SkillEffect(oreMultiplier = 1.2)),
    Skill("miner_2", "Iron Lungs", "Offline ore cap +1h", SkillTree.MINER, 2, requires = "miner_1",
        effect = SkillEffect(offlineCapBonus = 3600.0)),
    Skill("miner_3", "Vein Sight", "Ore rate +40%", SkillTree.MINER, 3, requires = "miner_2",
        effect = SkillEffect(oreMultiplier = 1.4)),
    Skill("builder_1", "Rapid Quarry", "Stone rate +20%", SkillTree.BUILDER, 1,
        effect = SkillEffect(stoneMultiplier = 1.2)),
    Skill("builder_2", "Efficient Cuts", "Stone rate +35%", SkillTree.BUILDER, 2, requires = "builder_1",
        effect = SkillEffect(stoneMultiplier = 1.35)),
    Skill("scholar_1", "Ancient Scripts", "Knowledge rate +30%", SkillTree.SCHOLAR, 1,
        effect = SkillEffect(knowledgeMultiplier = 1.3)),
    Skill("scholar_2", "Gnosis Boost", "XP gain +25%", SkillTree.SCHOLAR, 2, requires = "scholar_1",
        effect = SkillEffect(xpMultiplier = 1.25)),
)
