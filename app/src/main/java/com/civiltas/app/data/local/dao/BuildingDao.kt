package com.civiltas.app.data.local.dao

import androidx.room.*
import com.civiltas.app.data.local.entity.BuildingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {
    @Query("SELECT * FROM buildings")
    fun observeAll(): Flow<List<BuildingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(buildings: List<BuildingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(building: BuildingEntity)

    @Query("UPDATE buildings SET level = level + 1 WHERE id = :id")
    suspend fun upgradeBuilding(id: Long)

    @Query("UPDATE buildings SET isUnlocked = 1 WHERE type = :type")
    suspend fun unlockBuilding(type: String)
}
