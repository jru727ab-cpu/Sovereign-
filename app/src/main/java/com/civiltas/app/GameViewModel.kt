package com.civiltas.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * GameViewModel — bridges GameEngine (pure Kotlin) with Compose UI.
 * Runs a 1-second tick loop while the app is in the foreground.
 */
class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = GameEngine(SaveManager(application))

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    init {
        engine.loadAndApplyOfflineProgress()
        _state.value = engine.state
        startTickLoop()
    }

    private fun startTickLoop() {
        viewModelScope.launch {
            while (true) {
                delay(1_000L)
                engine.tick(1L)
                _state.value = engine.state
            }
        }
    }

    fun saveNow() = engine.save()

    override fun onCleared() {
        super.onCleared()
        engine.save()
    }
}
