package com.sovereign.civiltas.data.repository

import com.sovereign.civiltas.data.local.db.dao.GameStateDao
import com.sovereign.civiltas.data.local.db.entity.GameStateEntity
import com.sovereign.civiltas.domain.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val dao: GameStateDao
) {
    fun observeGameState(): Flow<GameState> =
        dao.observeGameState().map { it?.toDomain() ?: GameState() }

    suspend fun getGameState(): GameState =
        dao.getGameState()?.toDomain() ?: GameState()

    suspend fun saveGameState(state: GameState) =
        dao.saveGameState(state.toEntity())
}

private fun GameStateEntity.toDomain() = GameState(
    id = id,
    ore = ore, stone = stone, knowledge = knowledge, energy = energy,
    xp = xp, level = level,
    orePerSecond = orePerSecond, stonePerSecond = stonePerSecond,
    knowledgePerSecond = knowledgePerSecond, offlineCap = offlineCap,
    lastSeenEpoch = lastSeenEpoch,
    skillPoints = skillPoints, gnosisLevel = gnosisLevel,
    catastropheProgress = catastropheProgress,
    catastropheSeasonDay = catastropheSeasonDay,
    dailyCheckInStreak = dailyCheckInStreak, lastCheckInEpoch = lastCheckInEpoch,
    activeQuestIds = activeQuestIds.csvToList(),
    completedQuestIds = completedQuestIds.csvToList(),
    unlockedSkillIds = unlockedSkillIds.csvToList(),
    oreUpgradeLevel = oreUpgradeLevel,
    stoneUpgradeLevel = stoneUpgradeLevel,
    energyUpgradeLevel = energyUpgradeLevel,
    guestMode = guestMode,
    onlineSyncEnabled = onlineSyncEnabled
)

private fun GameState.toEntity() = GameStateEntity(
    id = id,
    ore = ore, stone = stone, knowledge = knowledge, energy = energy,
    xp = xp, level = level,
    orePerSecond = orePerSecond, stonePerSecond = stonePerSecond,
    knowledgePerSecond = knowledgePerSecond, offlineCap = offlineCap,
    lastSeenEpoch = lastSeenEpoch,
    skillPoints = skillPoints, gnosisLevel = gnosisLevel,
    catastropheProgress = catastropheProgress,
    catastropheSeasonDay = catastropheSeasonDay,
    dailyCheckInStreak = dailyCheckInStreak, lastCheckInEpoch = lastCheckInEpoch,
    activeQuestIds = activeQuestIds.listToCsv(),
    completedQuestIds = completedQuestIds.listToCsv(),
    unlockedSkillIds = unlockedSkillIds.listToCsv(),
    oreUpgradeLevel = oreUpgradeLevel,
    stoneUpgradeLevel = stoneUpgradeLevel,
    energyUpgradeLevel = energyUpgradeLevel,
    guestMode = guestMode,
    onlineSyncEnabled = onlineSyncEnabled
)

private fun String.csvToList(): List<String> =
    if (isBlank()) emptyList() else split(",").map { it.trim() }

private fun List<String>.listToCsv(): String = joinToString(",")
