package com.civiltas.app.data

import android.content.Context
import android.content.SharedPreferences
import com.civiltas.app.game.ALL_SECRETS
import org.json.JSONArray

class GameRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("civiltas_game", Context.MODE_PRIVATE)

    fun saveMiningState(state: MiningState) {
        prefs.edit().apply {
            putString("ore_balance", state.oreBalance.toString())
            putString("ore_per_second", state.orePerSecond.toString())
            putInt("upgrade_level", state.upgradeLevel)
            putLong("last_save_ms", state.lastSaveMs)
            apply()
        }
    }

    fun loadMiningState(): MiningState {
        return MiningState(
            oreBalance = prefs.getString("ore_balance", "0.0")?.toDoubleOrNull() ?: 0.0,
            orePerSecond = prefs.getString("ore_per_second", "0.1")?.toDoubleOrNull() ?: 0.1,
            upgradeLevel = prefs.getInt("upgrade_level", 0),
            lastSaveMs = prefs.getLong("last_save_ms", System.currentTimeMillis())
        )
    }

    fun saveUnlockedSecrets(unlockedIds: Set<String>) {
        val arr = JSONArray()
        unlockedIds.forEach { arr.put(it) }
        prefs.edit().putString("unlocked_secrets", arr.toString()).apply()
    }

    fun loadUnlockedSecrets(): Set<String> {
        val json = prefs.getString("unlocked_secrets", "[]") ?: "[]"
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { arr.getString(it) }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun loadGameState(): GameState {
        return GameState(
            miningState = loadMiningState(),
            unlockedSecretIds = loadUnlockedSecrets()
        )
    }

    fun saveGameState(state: GameState) {
        saveMiningState(state.miningState)
        saveUnlockedSecrets(state.unlockedSecretIds)
    }
}
