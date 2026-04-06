package com.civiltas.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buildings")
data class BuildingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val type: String,
    val level: Int = 1,
    val isUnlocked: Boolean = false,
    val isConstructing: Boolean = false,
    val constructionCompleteAt: Long? = null
)
