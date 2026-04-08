package com.civiltas.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val id: Long = 1L,
    val name: String = "Survivor",
    val level: Int = 1,
    val xp: Long = 0L,
    val civilizationName: String = "The Remnants",
    val daysSurvived: Int = 0,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val secretSocietyRank: Int = 0,
    val catastropheAlertLevel: Int = 0
)
