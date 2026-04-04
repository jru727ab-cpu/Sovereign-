package com.sovereign.wallet.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * WalletStorage – secure, encrypted local storage for wallet credentials.
 *
 * Uses [EncryptedSharedPreferences] backed by the Android Keystore so that
 * the mnemonic (seed phrase) and any other sensitive wallet data are never
 * stored in plain text on disk.
 *
 * All operations are synchronous and offline-only.  No data ever leaves the
 * device through this class.
 */
object WalletStorage {

    private const val PREFS_FILE = "sovereign_wallet_prefs"
    private const val KEY_MNEMONIC = "mnemonic"
    private const val KEY_WALLET_ADDRESS = "wallet_address"

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun prefs(context: Context) = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /** Persist the wallet mnemonic phrase (encrypted). */
    fun saveMnemonic(context: Context, mnemonic: String) {
        prefs(context).edit().putString(KEY_MNEMONIC, mnemonic).apply()
    }

    /** Retrieve the stored mnemonic phrase, or `null` if none has been saved. */
    fun getMnemonic(context: Context): String? =
        prefs(context).getString(KEY_MNEMONIC, null)

    /** Persist the wallet's public address. */
    fun saveWalletAddress(context: Context, address: String) {
        prefs(context).edit().putString(KEY_WALLET_ADDRESS, address).apply()
    }

    /** Retrieve the stored wallet address, or `null` if none has been saved. */
    fun getWalletAddress(context: Context): String? =
        prefs(context).getString(KEY_WALLET_ADDRESS, null)

    /** Returns `true` if a wallet has already been created/restored on this device. */
    fun hasWallet(context: Context): Boolean = getMnemonic(context) != null

    /**
     * Wipe all stored wallet data from this device.
     * Presents the user with a confirmation dialog before calling this.
     */
    fun clearAll(context: Context) {
        prefs(context).edit().clear().apply()
    }
}
