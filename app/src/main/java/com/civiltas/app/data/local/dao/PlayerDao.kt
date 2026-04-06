package com.civiltas.app.data.local.dao

import androidx.room.*
import com.civiltas.app.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE id = 1")
    fun observePlayer(): Flow<PlayerEntity?>

    @Query("SELECT * FROM players WHERE id = 1")
    suspend fun getPlayer(): PlayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlayer(player: PlayerEntity)

    @Query("UPDATE players SET xp = xp + :xpGain WHERE id = 1")
    suspend fun addXp(xpGain: Long)

    @Query("UPDATE players SET lastActiveTimestamp = :ts WHERE id = 1")
    suspend fun updateLastActive(ts: Long)
}
