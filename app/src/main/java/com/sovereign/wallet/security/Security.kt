package com.sovereign.wallet.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

/**
 * PIN and biometric security utilities for the wallet.
 * PINs are stored as SHA-256 hashes — never stored in plaintext.
 */
object Security {

    private const val SECURITY_PREFS = "sovereign_security_prefs"
    private const val KEY_PIN_HASH = "pin_hash"
    private const val KEY_PIN_ENABLED = "pin_enabled"
    private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"

    private fun getPrefs(context: Context): android.content.SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            SECURITY_PREFS,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /** Hash a PIN using SHA-256 (never store raw PIN). */
    private fun hashPin(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(pin.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }

    /** Save a new PIN (stores only the hash). */
    fun setPin(context: Context, pin: String) {
        getPrefs(context).edit()
            .putString(KEY_PIN_HASH, hashPin(pin))
            .putBoolean(KEY_PIN_ENABLED, true)
            .apply()
    }

    /** Verify user-entered PIN against stored hash. */
    fun verifyPin(context: Context, pin: String): Boolean {
        val storedHash = getPrefs(context).getString(KEY_PIN_HASH, null) ?: return false
        return hashPin(pin) == storedHash
    }

    /** Check whether PIN protection is enabled. */
    fun isPinEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_PIN_ENABLED, false)

    /** Remove PIN protection. */
    fun disablePin(context: Context) {
        getPrefs(context).edit()
            .remove(KEY_PIN_HASH)
            .putBoolean(KEY_PIN_ENABLED, false)
            .apply()
    }

    /** Enable or disable biometric unlock. */
    fun setBiometricEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit()
            .putBoolean(KEY_BIOMETRIC_ENABLED, enabled)
            .apply()
    }

    /** Check whether biometric unlock is enabled. */
    fun isBiometricEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_BIOMETRIC_ENABLED, false)
}
