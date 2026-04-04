package com.sovereign.civiltas.domain.model

data class GameState(
    val id: Int = 1,
    val ore: Double = 0.0,
    val stone: Double = 0.0,
    val knowledge: Double = 0.0,
    val energy: Double = 100.0,
    val xp: Long = 0L,
    val level: Int = 1,
    val orePerSecond: Double = 0.1,
    val stonePerSecond: Double = 0.05,
    val knowledgePerSecond: Double = 0.01,
    val offlineCap: Double = 3600.0, // max offline seconds of progress
    val lastSeenEpoch: Long = System.currentTimeMillis(),
    val skillPoints: Int = 0,
    val gnosisLevel: Int = 0,
    val catastropheProgress: Float = 0f, // 0..1
    val catastropheSeasonDay: Int = 0,
    val dailyCheckInStreak: Int = 0,
    val lastCheckInEpoch: Long = 0L,
    val activeQuestIds: List<String> = emptyList(),
    val completedQuestIds: List<String> = emptyList(),
    val unlockedSkillIds: List<String> = emptyList(),
    val oreUpgradeLevel: Int = 0,
    val stoneUpgradeLevel: Int = 0,
    val energyUpgradeLevel: Int = 0,
    val guestMode: Boolean = true,
    val onlineSyncEnabled: Boolean = false
)
