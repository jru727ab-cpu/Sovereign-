package com.civiltas.app.data.local.dao

import androidx.room.*
import com.civiltas.app.data.local.entity.DailyTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTaskDao {
    @Query("SELECT * FROM daily_tasks")
    fun observeAll(): Flow<List<DailyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tasks: List<DailyTaskEntity>)

    @Query("UPDATE daily_tasks SET isCompleted = 1 WHERE id = :id")
    suspend fun markCompleted(id: Long)

    @Query("UPDATE daily_tasks SET isCompleted = 0, resetAtTimestamp = :nextReset WHERE resetAtTimestamp < :now")
    suspend fun resetExpiredTasks(now: Long, nextReset: Long)
}
