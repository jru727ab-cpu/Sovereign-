package com.civiltas.app.data.repository

import com.civiltas.app.data.local.dao.*
import com.civiltas.app.data.local.entity.*
import com.civiltas.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class GameRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val resourceDao: ResourceDao,
    private val buildingDao: BuildingDao,
    private val dailyTaskDao: DailyTaskDao
) {
    // ── Player ────────────────────────────────────────────────────────────────

    fun observePlayer(): Flow<Player?> = playerDao.observePlayer().map { it?.toDomain() }

    suspend fun initPlayerIfNeeded() {
        if (playerDao.getPlayer() == null) {
            Timber.i("Initialising new player")
            playerDao.upsertPlayer(PlayerEntity())
            resourceDao.upsertAll(ResourceType.entries.map { rt ->
                ResourceEntity(type = rt.name, amount = 0.0, miningRatePerHour = defaultMiningRate(rt))
            })
            buildingDao.upsertAll(BuildingType.entries.map { bt ->
                BuildingEntity(type = bt.name, isUnlocked = bt == BuildingType.MINE)
            })
            val tomorrow = System.currentTimeMillis() + ONE_DAY_MILLIS
            dailyTaskDao.upsertAll(defaultDailyTasks.map { it.toEntity(tomorrow) })
        }
    }

    suspend fun addXp(amount: Long) {
        playerDao.addXp(amount)
        val player = playerDao.getPlayer() ?: return
        val required = xpRequiredForLevel(player.level)
        if (player.xp >= required) {
            playerDao.upsertPlayer(
                player.copy(level = player.level + 1, xp = player.xp - required)
            )
            Timber.i("Player levelled up to ${player.level + 1}")
        }
    }

    // ── Offline earnings ──────────────────────────────────────────────────────

    suspend fun applyOfflineEarnings() {
        val player = playerDao.getPlayer() ?: return
        val now = System.currentTimeMillis()
        val elapsedHours = (now - player.lastActiveTimestamp) / MILLIS_PER_HOUR
        val cappedHours = min(elapsedHours, MAX_OFFLINE_HOURS)
        if (cappedHours < 0.01) return

        Timber.i("Applying %.2f hours offline earnings".format(cappedHours))

        val dbResources = getAllResources()
        dbResources.forEach { r ->
            val gain = r.miningRatePerHour * cappedHours
            resourceDao.addAmount(r.type, gain)
        }
        playerDao.updateLastActive(now)
        addXp((cappedHours * XP_PER_HOUR).toLong())
    }

    private suspend fun getAllResources(): List<ResourceEntity> {
        val result = mutableListOf<ResourceEntity>()
        ResourceType.entries.forEach { rt ->
            resourceDao.getByType(rt.name)?.let { result.add(it) }
        }
        return result
    }

    // ── Resources ─────────────────────────────────────────────────────────────

    fun observeResources(): Flow<List<Resource>> =
        resourceDao.observeAll().map { list -> list.map { it.toDomain() } }

    // ── Buildings ─────────────────────────────────────────────────────────────

    fun observeBuildings(): Flow<List<Building>> =
        buildingDao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun upgradeBuilding(id: Long) {
        buildingDao.upgradeBuilding(id)
        addXp(XP_PER_UPGRADE)
    }

    // ── Daily tasks ───────────────────────────────────────────────────────────

    fun observeDailyTasks(): Flow<List<DailyTask>> =
        dailyTaskDao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun completeTask(id: Long, xpReward: Long) {
        dailyTaskDao.markCompleted(id)
        addXp(xpReward)
    }

    suspend fun resetExpiredTasks() {
        val now = System.currentTimeMillis()
        val tomorrow = now + ONE_DAY_MILLIS
        dailyTaskDao.resetExpiredTasks(now, tomorrow)
    }

    companion object {
        const val MAX_OFFLINE_HOURS = 8.0
        const val XP_PER_HOUR = 20.0
        const val XP_PER_UPGRADE = 50L
        const val MILLIS_PER_HOUR = 3_600_000.0
        const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L

        fun defaultMiningRate(type: ResourceType): Double = when (type) {
            ResourceType.IRON -> 10.0
            ResourceType.COPPER -> 8.0
            ResourceType.COAL -> 12.0
            ResourceType.STONE -> 15.0
            ResourceType.LUMBER -> 6.0
            ResourceType.FOOD -> 20.0
            ResourceType.WATER -> 25.0
            ResourceType.GOLD -> 2.0
            ResourceType.OIL -> 3.0
            ResourceType.KNOWLEDGE -> 1.0
            ResourceType.ANCIENT_RELIC -> 0.1
            ResourceType.GNOSIS -> 0.01
        }
    }
}

// ── Mappers ───────────────────────────────────────────────────────────────────

private fun PlayerEntity.toDomain() = Player(
    id = id, name = name, level = level, xp = xp,
    xpToNextLevel = xpRequiredForLevel(level),
    civilizationName = civilizationName, daysSurvived = daysSurvived,
    lastActiveTimestamp = lastActiveTimestamp,
    secretSocietyRank = secretSocietyRank,
    catastropheAlertLevel = catastropheAlertLevel
)

private fun ResourceEntity.toDomain() = Resource(
    type = ResourceType.valueOf(type),
    amount = amount,
    miningRatePerHour = miningRatePerHour
)

private fun BuildingEntity.toDomain() = Building(
    id = id, type = BuildingType.valueOf(type), level = level,
    isUnlocked = isUnlocked, isConstructing = isConstructing,
    constructionCompleteAt = constructionCompleteAt
)

private fun DailyTaskEntity.toDomain() = DailyTask(
    id = id, title = title, description = description,
    xpReward = xpReward, resourceReward = resourceReward,
    isCompleted = isCompleted, resetAtTimestamp = resetAtTimestamp
)

private fun DailyTask.toEntity(resetAt: Long) = DailyTaskEntity(
    id = id, title = title, description = description,
    xpReward = xpReward, resourceReward = resourceReward,
    isCompleted = isCompleted, resetAtTimestamp = resetAt
)
