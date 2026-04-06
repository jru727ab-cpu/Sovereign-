package com.civiltas.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civiltas.app.data.repository.GameRepository
import com.civiltas.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class GameUiState(
    val player: Player = Player(),
    val resources: List<Resource> = emptyList(),
    val buildings: List<Building> = emptyList(),
    val dailyTasks: List<DailyTask> = emptyList(),
    val isLoading: Boolean = true,
    val offlineEarningsMessage: String? = null
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.initPlayerIfNeeded()
            gameRepository.applyOfflineEarnings()
            gameRepository.resetExpiredTasks()
            collectFlows()
        }
    }

    private suspend fun collectFlows() {
        combine(
            gameRepository.observePlayer(),
            gameRepository.observeResources(),
            gameRepository.observeBuildings(),
            gameRepository.observeDailyTasks()
        ) { player, resources, buildings, tasks ->
            GameUiState(
                player = player ?: Player(),
                resources = resources,
                buildings = buildings,
                dailyTasks = tasks,
                isLoading = false
            )
        }.collect { state ->
            _uiState.value = state
        }
    }

    fun upgradeBuilding(id: Long) {
        viewModelScope.launch {
            Timber.d("Upgrading building $id")
            gameRepository.upgradeBuilding(id)
        }
    }

    fun completeTask(id: Long, xpReward: Long) {
        viewModelScope.launch {
            Timber.d("Completing task $id")
            gameRepository.completeTask(id, xpReward)
        }
    }

    fun dismissOfflineMessage() {
        _uiState.update { it.copy(offlineEarningsMessage = null) }
    }
}
