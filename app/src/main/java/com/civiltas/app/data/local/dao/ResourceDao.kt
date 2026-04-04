package com.civiltas.app.data.local.dao

import androidx.room.*
import com.civiltas.app.data.local.entity.ResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {
    @Query("SELECT * FROM resources")
    fun observeAll(): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE type = :type")
    suspend fun getByType(type: String): ResourceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(resources: List<ResourceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(resource: ResourceEntity)

    @Query("UPDATE resources SET amount = amount + :delta WHERE type = :type")
    suspend fun addAmount(type: String, delta: Double)
}
