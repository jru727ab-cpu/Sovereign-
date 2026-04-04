package com.sovereign.wallet.viewmodels

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sovereign.wallet.data.LocalFileManager
import com.sovereign.wallet.data.WalletStorage
import com.sovereign.wallet.security.Security
import com.sovereign.wallet.utils.BugCleaner
import com.sovereign.wallet.utils.WalletUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sovereign_settings")

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>().applicationContext

    // --- Wallet state ---
    private val _walletAddress = MutableStateFlow<String?>(null)
    val walletAddress: StateFlow<String?> = _walletAddress.asStateFlow()

    private val _mnemonic = MutableStateFlow<String?>(null)
    val mnemonic: StateFlow<String?> = _mnemonic.asStateFlow()

    private val _walletName = MutableStateFlow("My Wallet")
    val walletName: StateFlow<String> = _walletName.asStateFlow()

    private val _hasWallet = MutableStateFlow(false)
    val hasWallet: StateFlow<Boolean> = _hasWallet.asStateFlow()

    // --- Security state ---
    private val _isPinEnabled = MutableStateFlow(false)
    val isPinEnabled: StateFlow<Boolean> = _isPinEnabled.asStateFlow()

    private val _isBiometricEnabled = MutableStateFlow(false)
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    // --- Settings state ---
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // --- Bug cleaner state ---
    private val _diagnosticsReport = MutableStateFlow<BugCleaner.DiagnosticsReport?>(null)
    val diagnosticsReport: StateFlow<BugCleaner.DiagnosticsReport?> = _diagnosticsReport.asStateFlow()

    private val _cleanerRunning = MutableStateFlow(false)
    val cleanerRunning: StateFlow<Boolean> = _cleanerRunning.asStateFlow()

    private val _storageUsedMb = MutableStateFlow(0.0)
    val storageUsedMb: StateFlow<Double> = _storageUsedMb.asStateFlow()

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    init {
        loadWallet()
        loadSecurityState()
        loadSettings()
        refreshStorageStats()
    }

    private fun loadWallet() {
        val savedMnemonic = WalletStorage.getMnemonic(context)
        if (savedMnemonic != null) {
            _hasWallet.value = true
            _walletAddress.value = WalletStorage.getAddress(context)
                ?: WalletUtils.mnemonicToAddress(savedMnemonic)
            _walletName.value = WalletStorage.getWalletName(context)
        }
    }

    private fun loadSecurityState() {
        _isPinEnabled.value = Security.isPinEnabled(context)
        _isBiometricEnabled.value = Security.isBiometricEnabled(context)
        // Auto-unlock if no security is configured
        if (!_isPinEnabled.value && !_isBiometricEnabled.value) {
            _isUnlocked.value = true
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val prefs = context.dataStore.data.first()
            _isDarkMode.value = prefs[DARK_MODE_KEY] ?: true
        }
    }

    fun createWallet() {
        val newMnemonic = WalletUtils.generateMnemonic()
        val address = WalletUtils.mnemonicToAddress(newMnemonic)
        WalletStorage.saveMnemonic(context, newMnemonic)
        WalletStorage.saveAddress(context, address)
        _mnemonic.value = newMnemonic
        _walletAddress.value = address
        _hasWallet.value = true
    }

    fun restoreWallet(mnemonicPhrase: String): Boolean {
        if (!WalletUtils.isValidMnemonic(mnemonicPhrase)) return false
        val address = WalletUtils.mnemonicToAddress(mnemonicPhrase)
        WalletStorage.saveMnemonic(context, mnemonicPhrase)
        WalletStorage.saveAddress(context, address)
        _mnemonic.value = mnemonicPhrase
        _walletAddress.value = address
        _hasWallet.value = true
        return true
    }

    fun getMnemonicForDisplay(): String? = WalletStorage.getMnemonic(context)

    fun deleteWallet() {
        WalletStorage.clearWallet(context)
        _hasWallet.value = false
        _walletAddress.value = null
        _mnemonic.value = null
    }

    fun setPin(pin: String) {
        Security.setPin(context, pin)
        _isPinEnabled.value = true
    }

    fun verifyPin(pin: String): Boolean = Security.verifyPin(context, pin)

    fun disablePin() {
        Security.disablePin(context)
        _isPinEnabled.value = false
    }

    fun setBiometricEnabled(enabled: Boolean) {
        Security.setBiometricEnabled(context, enabled)
        _isBiometricEnabled.value = enabled
    }

    fun unlock() {
        _isUnlocked.value = true
    }

    fun lock() {
        _isUnlocked.value = false
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[DARK_MODE_KEY] = enabled
            }
        }
    }

    fun runDiagnostics() {
        viewModelScope.launch {
            _cleanerRunning.value = true
            _diagnosticsReport.value = BugCleaner.runDiagnostics(context)
            _cleanerRunning.value = false
        }
    }

    fun runFullCleanup(): String {
        val cacheCleared = BugCleaner.clearCache(context)
        val externalCleared = BugCleaner.clearExternalCache(context)
        val tempFilesRemoved = BugCleaner.cleanTempFiles(context)
        refreshStorageStats()
        return buildString {
            append(if (cacheCleared) "✅ Cache cleared\n" else "⚠️ Could not clear cache\n")
            append(if (externalCleared) "✅ External cache cleared\n" else "ℹ️ No external cache\n")
            append("✅ Removed $tempFilesRemoved temp file(s)\n")
        }.trim()
    }

    fun refreshStorageStats() {
        _storageUsedMb.value = LocalFileManager.getTotalStorageUsedBytes(context) / (1024.0 * 1024.0)
    }
}
