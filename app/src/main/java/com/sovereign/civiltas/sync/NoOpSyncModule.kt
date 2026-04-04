package com.sovereign.civiltas.sync

import com.sovereign.civiltas.domain.model.GameState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoOpSyncModule @Inject constructor() : SyncModule {
    override suspend fun uploadState(state: GameState): Result<Unit> = Result.success(Unit)
    override suspend fun downloadState(): Result<GameState?> = Result.success(null)
    override fun isSyncAvailable(): Boolean = false
}
