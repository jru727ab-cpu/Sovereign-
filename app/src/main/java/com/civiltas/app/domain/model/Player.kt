package com.civiltas.app.domain.model

data class Player(
    val id: Long = 1L,
    val name: String = "Survivor",
    val level: Int = 1,
    val xp: Long = 0L,
    val xpToNextLevel: Long = 100L,
    val civilizationName: String = "The Remnants",
    val daysSurvived: Int = 0,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val secretSocietyRank: Int = 0,
    val catastropheAlertLevel: Int = 0  // 0-10 scale
)

fun xpRequiredForLevel(level: Int): Long = (100L * level * (level + 1)) / 2
