package com.sovereign.civiltas.domain.model

enum class QuestType { DAILY, ROTATING, STORY }

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val type: QuestType,
    val requireOre: Double = 0.0,
    val requireStone: Double = 0.0,
    val requireKnowledge: Double = 0.0,
    val rewardOre: Double = 0.0,
    val rewardStone: Double = 0.0,
    val rewardXp: Long = 0L,
    val rewardSkillPoints: Int = 0,
    val isCompleted: Boolean = false
)

val DAILY_QUESTS = listOf(
    Quest("daily_mine_100", "Ore Rush", "Mine 100 ore today.", QuestType.DAILY,
        requireOre = 100.0, rewardXp = 200L, rewardStone = 20.0),
    Quest("daily_stone_50", "Stone Age", "Collect 50 stone.", QuestType.DAILY,
        requireStone = 50.0, rewardXp = 150L, rewardOre = 30.0),
    Quest("daily_checkin", "Survivor's Log", "Daily check-in streak.", QuestType.DAILY,
        rewardXp = 100L, rewardSkillPoints = 1),
)

val ROTATING_QUESTS = listOf(
    Quest("rot_knowledge_20", "Scholar's Path", "Accumulate 20 knowledge.", QuestType.ROTATING,
        requireKnowledge = 20.0, rewardXp = 500L, rewardSkillPoints = 1),
    Quest("rot_ore_500", "The Great Mine", "Mine 500 ore.", QuestType.ROTATING,
        requireOre = 500.0, rewardXp = 800L, rewardStone = 100.0),
    Quest("rot_upgrade_3", "Builder's Pride", "Reach upgrade level 3.", QuestType.ROTATING,
        rewardXp = 600L, rewardOre = 200.0),
    Quest("rot_lvl5", "Rising Sovereign", "Reach player level 5.", QuestType.ROTATING,
        rewardXp = 1000L, rewardSkillPoints = 2),
)
