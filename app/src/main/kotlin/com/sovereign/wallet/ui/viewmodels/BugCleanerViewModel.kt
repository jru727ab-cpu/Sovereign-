package com.sovereign.wallet.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sovereign.wallet.utils.BugCleaner
import com.sovereign.wallet.utils.CleanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * State holder for [com.sovereign.wallet.ui.screens.BugCleanerScreen].
 *
 * Uses [AndroidViewModel] so it can safely hold a reference to the application
 * [Context] needed by [BugCleaner] without leaking an Activity.
 */
class BugCleanerViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(BugCleanerUiState())
    val uiState: StateFlow<BugCleanerUiState> = _uiState.asStateFlow()

    /**
     * Kick off a full maintenance pass on the IO dispatcher.
     * Guards against concurrent runs via [BugCleanerUiState.isRunning].
     */
    fun startCleaning() {
        if (_uiState.value.isRunning) return
        _uiState.update { it.copy(isRunning = true, result = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = BugCleaner.runAll(getApplication())
            _uiState.update { it.copy(isRunning = false, result = result) }
        }
    }
}

/**
 * Immutable UI state for the Bug Cleaner screen.
 *
 * @param isRunning `true` while a cleaning pass is executing.
 * @param result    The result of the most recent pass, or `null` if none has run yet.
 */
data class BugCleanerUiState(
    val isRunning: Boolean = false,
    val result: CleanResult? = null
)
