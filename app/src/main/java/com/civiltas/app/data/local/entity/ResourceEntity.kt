package com.civiltas.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resources")
data class ResourceEntity(
    @PrimaryKey val type: String,
    val amount: Double = 0.0,
    val miningRatePerHour: Double = 1.0
)
