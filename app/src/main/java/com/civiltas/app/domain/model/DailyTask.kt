package com.civiltas.app.domain.model

data class DailyTask(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val xpReward: Long,
    val resourceReward: Double = 0.0,
    val isCompleted: Boolean = false,
    val resetAtTimestamp: Long = 0L
)

val defaultDailyTasks = listOf(
    DailyTask(1L, "Daily Check-In", "Open the app today", 25L, 0.0),
    DailyTask(2L, "Gather Resources", "Collect offline earnings", 50L, 10.0),
    DailyTask(3L, "Build Something", "Upgrade or build a structure", 75L, 0.0),
    DailyTask(4L, "Research", "Complete a research item", 100L, 0.0),
    DailyTask(5L, "Decode a Relic", "Uncover a piece of ancient knowledge", 150L, 5.0)
)
