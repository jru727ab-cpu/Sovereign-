package com.civiltas.app

import android.content.Context
import android.content.SharedPreferences

/**
 * SaveManager — lightweight SharedPreferences wrapper.
 * MVP persistence: no schema migrations, no ORM overhead.
 * Swap for RoomStorageBackend in Phase 2 by implementing StorageBackend.
 */
class SaveManager(context: Context) : StorageBackend {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun loadLong(key: String, default: Long) = prefs.getLong(key, default)
    override fun saveLong(key: String, value: Long) { prefs.edit().putLong(key, value).apply() }

    override fun loadFloat(key: String, default: Float) = prefs.getFloat(key, default)
    override fun saveFloat(key: String, value: Float) { prefs.edit().putFloat(key, value).apply() }

    override fun loadInt(key: String, default: Int) = prefs.getInt(key, default)
    override fun saveInt(key: String, value: Int) { prefs.edit().putInt(key, value).apply() }

    companion object {
        private const val PREFS_NAME = "civiltas_save"
    }
}
