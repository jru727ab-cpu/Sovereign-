package com.civiltas.app.data.local.dao

import androidx.room.*
import com.civiltas.app.data.local.entity.DiagnosticLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticLogDao {
    @Query("SELECT * FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 500")
    fun observeRecent(): Flow<List<DiagnosticLogEntity>>

    @Query("SELECT * FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 500")
    suspend fun getRecent(): List<DiagnosticLogEntity>

    @Insert
    suspend fun insert(log: DiagnosticLogEntity)

    @Query("DELETE FROM diagnostic_logs WHERE timestamp < :cutoff")
    suspend fun pruneOlderThan(cutoff: Long)
}
