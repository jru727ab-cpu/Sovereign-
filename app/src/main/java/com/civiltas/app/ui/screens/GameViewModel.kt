package com.civiltas.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.civiltas.app.data.GameRepository
import com.civiltas.app.data.GameState
import com.civiltas.app.data.MiningState
import com.civiltas.app.data.SecretEntry
import com.civiltas.app.game.ALL_SECRETS
import com.civiltas.app.game.IdleEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val miningState: MiningState = MiningState(),
    val secrets: List<SecretEntry> = ALL_SECRETS,
    val offlineGainPopup: Double? = null
)

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var tickJob: Job? = null

    init {
        loadAndInit()
    }

    private fun loadAndInit() {
        val saved = repository.loadGameState()
        val nowMs = System.currentTimeMillis()
        val offlineGain = if (saved.miningState.lastSaveMs > 0L) {
            IdleEngine.calcOfflineGain(saved.miningState.orePerSecond, saved.miningState.lastSaveMs, nowMs)
        } else 0.0

        val updatedMining = saved.miningState.copy(
            oreBalance = saved.miningState.oreBalance + offlineGain,
            lastSaveMs = nowMs
        )
        val mergedSecrets = ALL_SECRETS.map { it.copy(isUnlocked = it.id in saved.unlockedSecretIds) }

        _uiState.update {
            it.copy(
                miningState = updatedMining,
                secrets = mergedSecrets,
                offlineGainPopup = if (offlineGain > 0.01) offlineGain else null
            )
        }
        startTicking()
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { state ->
                    val gain = state.miningState.orePerSecond
                    state.copy(
                        miningState = state.miningState.copy(
                            oreBalance = state.miningState.oreBalance + gain
                        )
                    )
                }
                saveState()
            }
        }
    }

    fun collectOre() {
        _uiState.update { state ->
            val bonus = IdleEngine.manualCollectBonus(state.miningState.orePerSecond)
            state.copy(miningState = state.miningState.copy(oreBalance = state.miningState.oreBalance + bonus))
        }
        saveState()
    }

    fun purchaseUpgrade() {
        val state = _uiState.value
        val nextLevel = state.miningState.upgradeLevel + 1
        val cost = IdleEngine.upgradeCost(state.miningState.upgradeLevel)
        if (state.miningState.oreBalance >= cost) {
            val newOps = IdleEngine.upgradeOPS(0.1, nextLevel)
            _uiState.update {
                it.copy(
                    miningState = it.miningState.copy(
                        oreBalance = it.miningState.oreBalance - cost,
                        orePerSecond = newOps,
                        upgradeLevel = nextLevel
                    )
                )
            }
            saveState()
        }
    }

    fun dismissOfflinePopup() {
        _uiState.update { it.copy(offlineGainPopup = null) }
    }

    fun unlockSecret(id: String) {
        _uiState.update { state ->
            state.copy(secrets = state.secrets.map { s ->
                if (s.id == id) s.copy(isUnlocked = true) else s
            })
        }
        saveState()
    }

    private fun saveState() {
        val state = _uiState.value
        val unlockedIds = state.secrets.filter { it.isUnlocked }.map { it.id }.toSet()
        repository.saveGameState(
            GameState(
                miningState = state.miningState.copy(lastSaveMs = System.currentTimeMillis()),
                unlockedSecretIds = unlockedIds
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        tickJob?.cancel()
        saveState()
    }
}

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GameViewModel(repository) as T
    }
}
