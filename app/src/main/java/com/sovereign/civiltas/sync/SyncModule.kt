package com.sovereign.civiltas.sync

import com.sovereign.civiltas.domain.model.GameState

interface SyncModule {
    suspend fun uploadState(state: GameState): Result<Unit>
    suspend fun downloadState(): Result<GameState?>
    fun isSyncAvailable(): Boolean
}
