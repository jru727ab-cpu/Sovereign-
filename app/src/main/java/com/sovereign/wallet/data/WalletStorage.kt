package com.sovereign.wallet.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Secure, encrypted local storage for wallet keys and mnemonic phrases.
 * Uses AES-256-GCM encryption via Android Keystore — never writes plaintext keys to disk.
 */
object WalletStorage {

    private const val PREFS_FILE = "sovereign_wallet_prefs"
    private const val KEY_MNEMONIC = "wallet_mnemonic"
    private const val KEY_ADDRESS = "wallet_address"
    private const val KEY_WALLET_NAME = "wallet_name"
    private const val KEY_WALLET_COUNT = "wallet_count"

    private fun getPrefs(context: Context): android.content.SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveMnemonic(context: Context, mnemonic: String) {
        getPrefs(context).edit().putString(KEY_MNEMONIC, mnemonic).apply()
    }

    fun getMnemonic(context: Context): String? =
        getPrefs(context).getString(KEY_MNEMONIC, null)

    fun saveAddress(context: Context, address: String) {
        getPrefs(context).edit().putString(KEY_ADDRESS, address).apply()
    }

    fun getAddress(context: Context): String? =
        getPrefs(context).getString(KEY_ADDRESS, null)

    fun saveWalletName(context: Context, name: String) {
        getPrefs(context).edit().putString(KEY_WALLET_NAME, name).apply()
    }

    fun getWalletName(context: Context): String =
        getPrefs(context).getString(KEY_WALLET_NAME, "My Wallet") ?: "My Wallet"

    fun hasWallet(context: Context): Boolean =
        getMnemonic(context) != null

    /** Securely wipe all wallet data from device storage. */
    fun clearWallet(context: Context) {
        getPrefs(context).edit().clear().apply()
    }

    /** Increment wallet count for multi-wallet support stub */
    fun getWalletCount(context: Context): Int =
        getPrefs(context).getInt(KEY_WALLET_COUNT, 0)

    fun incrementWalletCount(context: Context) {
        val count = getWalletCount(context) + 1
        getPrefs(context).edit().putInt(KEY_WALLET_COUNT, count).apply()
    }
}
