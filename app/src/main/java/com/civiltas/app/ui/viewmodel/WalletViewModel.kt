package com.civiltas.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class WalletUiState(
    val connectedAddress: String? = null,
    val coldWalletAddress: String = "Not configured",
    val connectionStatus: String = "Disconnected",
    val isConnecting: Boolean = false
)

@HiltViewModel
class WalletViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    /**
     * Initiates a WalletConnect deep-link connection.
     * In production: integrate WalletConnect SDK or a compatible deep-link scheme.
     * NEVER store or handle private keys in this app.
     */
    fun connectWallet(address: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, connectionStatus = "Connecting…") }
            // TODO: replace with real WalletConnect / deep-link handshake
            Timber.i("WalletViewModel: connecting to $address (stub)")
            kotlinx.coroutines.delay(1_000)
            _uiState.update {
                it.copy(
                    connectedAddress = address.ifBlank { null },
                    connectionStatus = if (address.isNotBlank()) "Connected" else "Disconnected",
                    isConnecting = false
                )
            }
        }
    }

    fun disconnectWallet() {
        Timber.i("WalletViewModel: disconnecting wallet")
        _uiState.update { WalletUiState() }
    }

    fun setColdWalletAddress(address: String) {
        _uiState.update { it.copy(coldWalletAddress = address) }
    }
}
