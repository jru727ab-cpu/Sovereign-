package com.sovereign.civiltas.data.local.db.dao

import androidx.room.*
import com.sovereign.civiltas.data.local.db.entity.GameStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStateDao {
    @Query("SELECT * FROM game_state WHERE id = 1")
    fun observeGameState(): Flow<GameStateEntity?>

    @Query("SELECT * FROM game_state WHERE id = 1")
    suspend fun getGameState(): GameStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGameState(entity: GameStateEntity)
}
