package com.civiltas.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_tasks")
data class DailyTaskEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val xpReward: Long,
    val resourceReward: Double = 0.0,
    val isCompleted: Boolean = false,
    val resetAtTimestamp: Long = 0L
)
