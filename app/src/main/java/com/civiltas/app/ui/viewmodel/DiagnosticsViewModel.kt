package com.civiltas.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civiltas.app.data.local.dao.DiagnosticLogDao
import com.civiltas.app.data.local.entity.DiagnosticLogEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticsViewModel @Inject constructor(
    private val diagnosticLogDao: DiagnosticLogDao
) : ViewModel() {

    val logs: StateFlow<List<DiagnosticLogEntity>> = diagnosticLogDao
        .observeRecent()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun exportLogs(onReady: (String) -> Unit) {
        viewModelScope.launch {
            val allLogs = diagnosticLogDao.getRecent()
            val text = buildString {
                appendLine("=== CIVILTAS Diagnostic Report ===")
                appendLine("Exported: ${java.util.Date()}")
                appendLine()
                allLogs.forEach { log ->
                    appendLine("[${log.level}] ${java.util.Date(log.timestamp)} [${log.tag}] ${log.message}")
                    log.stackTrace?.let { appendLine(it) }
                }
            }
            onReady(text)
        }
    }
}
