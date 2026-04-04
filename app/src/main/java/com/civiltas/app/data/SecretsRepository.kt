package com.civiltas.app.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Simple SharedPreferences-backed repository for tracking which Secrets have been unlocked.
 *
 * Uses a flat key-value format: "secret_unlocked_<id>" → Boolean.
 * No external dependencies — suitable for the offline-first MVP.
 */
class SecretsRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /** Returns true if the secret with the given id has been unlocked. */
    fun isUnlocked(secretId: String): Boolean =
        prefs.getBoolean(unlockKey(secretId), false)

    /** Marks a secret as unlocked and persists it immediately. */
    fun unlock(secretId: String) {
        prefs.edit().putBoolean(unlockKey(secretId), true).apply()
    }

    /** Returns the set of all currently unlocked secret ids. */
    fun unlockedIds(): Set<String> =
        SecretsCatalog.all
            .filter { isUnlocked(it.id) }
            .map { it.id }
            .toSet()

    private fun unlockKey(secretId: String) = "${KEY_PREFIX}$secretId"

    companion object {
        private const val PREFS_NAME = "civiltas_secrets"
        private const val KEY_PREFIX = "secret_unlocked_"
    }
}
