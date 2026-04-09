package com.sovereign.civiltas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sovereign.civiltas.data.repository.GameRepository
import com.sovereign.civiltas.diagnostics.CiviltasLogger
import com.sovereign.civiltas.domain.engine.*
import com.sovereign.civiltas.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _offlineGains = MutableStateFlow<OfflineGains?>(null)
    val offlineGains: StateFlow<OfflineGains?> = _offlineGains.asStateFlow()

    private var tickJob: Job? = null

    init {
        viewModelScope.launch {
            val saved = repo.getGameState()
            val now = System.currentTimeMillis()
            val gains = OfflineProgressEngine.computeOfflineGains(saved, now)
            if (gains.oreGained > MIN_OFFLINE_GAIN_THRESHOLD || gains.stoneGained > MIN_OFFLINE_GAIN_THRESHOLD) {
                _offlineGains.value = gains
            }
            val updated = OfflineProgressEngine.applyGains(saved, gains, now)
            val rates = ResourceEngine.computeRates(updated)
            _state.value = updated.copy(
                orePerSecond = rates.orePerSecond,
                stonePerSecond = rates.stonePerSecond,
                knowledgePerSecond = rates.knowledgePerSecond,
                offlineCap = rates.offlineCap,
                level = ResourceEngine.levelFromXp(updated.xp)
            )
            CiviltasLogger.i("Game loaded. Level=${_state.value.level} Ore=${_state.value.ore}")
            startTicking()
        }
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                tick()
            }
        }
    }

    private fun tick() {
        val s = _state.value
        val newOre = s.ore + s.orePerSecond
        val newStone = s.stone + s.stonePerSecond
        val newKnowledge = s.knowledge + s.knowledgePerSecond
        val newXp = s.xp + ONLINE_XP_PER_TICK
        val newLevel = ResourceEngine.levelFromXp(newXp)
        val newSkillPoints = if (newLevel > s.level) s.skillPoints + 1 else s.skillPoints
        val afterCatastrophe = CatastropheEngine.tick(
            s.copy(ore = newOre, stone = newStone, knowledge = newKnowledge,
                   xp = newXp, level = newLevel, skillPoints = newSkillPoints), 1.0
        )
        _state.value = afterCatastrophe
    }

    fun collectOre() {
        _state.update { it.copy(ore = it.ore + it.orePerSecond * 5) }
    }

    fun buyUpgrade(upgradeId: String) {
        val s = _state.value
        when (upgradeId) {
            "ore_extractor" -> {
                val upgrade = oreUpgrades(s.oreUpgradeLevel)
                if (s.ore >= upgrade.oreCost && s.stone >= upgrade.stoneCost) {
                    val newState = s.copy(
                        ore = s.ore - upgrade.oreCost,
                        stone = s.stone - upgrade.stoneCost,
                        oreUpgradeLevel = s.oreUpgradeLevel + 1
                    )
                    val rates = ResourceEngine.computeRates(newState)
                    _state.value = newState.copy(orePerSecond = rates.orePerSecond)
                    CiviltasLogger.i("Upgraded ore_extractor to level ${s.oreUpgradeLevel + 1}")
                }
            }
            "stone_drill" -> {
                val upgrade = stoneUpgrades(s.stoneUpgradeLevel)
                if (s.ore >= upgrade.oreCost && s.stone >= upgrade.stoneCost) {
                    val newState = s.copy(
                        ore = s.ore - upgrade.oreCost,
                        stone = s.stone - upgrade.stoneCost,
                        stoneUpgradeLevel = s.stoneUpgradeLevel + 1
                    )
                    val rates = ResourceEngine.computeRates(newState)
                    _state.value = newState.copy(stonePerSecond = rates.stonePerSecond)
                    CiviltasLogger.i("Upgraded stone_drill to level ${s.stoneUpgradeLevel + 1}")
                }
            }
            "power_cell" -> {
                val upgrade = energyUpgrades(s.energyUpgradeLevel)
                if (s.ore >= upgrade.oreCost && s.stone >= upgrade.stoneCost) {
                    val newState = s.copy(
                        ore = s.ore - upgrade.oreCost,
                        stone = s.stone - upgrade.stoneCost,
                        energyUpgradeLevel = s.energyUpgradeLevel + 1
                    )
                    val rates = ResourceEngine.computeRates(newState)
                    _state.value = newState.copy(offlineCap = rates.offlineCap)
                    CiviltasLogger.i("Upgraded power_cell to level ${s.energyUpgradeLevel + 1}")
                }
            }
        }
    }

    fun unlockSkill(skillId: String) {
        val s = _state.value
        val skill = ALL_SKILLS.find { it.id == skillId } ?: return
        if (skill.id in s.unlockedSkillIds) return
        if (s.skillPoints < skill.cost) return
        if (skill.requires != null && skill.requires !in s.unlockedSkillIds) return

        val newState = s.copy(
            skillPoints = s.skillPoints - skill.cost,
            unlockedSkillIds = s.unlockedSkillIds + skillId
        )
        val rates = ResourceEngine.computeRates(newState)
        _state.value = newState.copy(
            orePerSecond = rates.orePerSecond,
            stonePerSecond = rates.stonePerSecond,
            knowledgePerSecond = rates.knowledgePerSecond,
            offlineCap = rates.offlineCap
        )
        CiviltasLogger.i("Unlocked skill: $skillId")
    }

    fun claimQuest(quest: Quest) {
        val newState = QuestEngine.claimQuest(quest, _state.value)
        _state.value = newState
        viewModelScope.launch { repo.saveGameState(newState) }
    }

    fun performDailyCheckIn() {
        val now = System.currentTimeMillis()
        val newState = QuestEngine.performDailyCheckIn(_state.value, now)
        _state.value = newState
        viewModelScope.launch { repo.saveGameState(newState) }
    }

    fun dismissOfflineGains() { _offlineGains.value = null }

    fun saveNow() {
        viewModelScope.launch {
            repo.saveGameState(_state.value.copy(lastSeenEpoch = System.currentTimeMillis()))
        }
    }

    companion object {
        private const val MIN_OFFLINE_GAIN_THRESHOLD = 0.01
        // Online tick awards 1 XP/s; offline tick awards 0.5 XP/s to reward active play
        private const val ONLINE_XP_PER_TICK = 1L
    }

    override fun onCleared() {
        super.onCleared()
        saveNow()
    }
}
