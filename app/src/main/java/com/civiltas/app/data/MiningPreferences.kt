package com.civiltas.app.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Abstraction over mining persistence so that [com.civiltas.app.MiningViewModel]
 * can be tested without an Android context.
 */
interface MiningPreferences {
    var totalOre: Long
    var orePerTap: Long
    var passiveOrePerHour: Long

    /** System-clock millis when the app last went to background. */
    var lastTimestampMillis: Long

    /** Total lifetime tap count, persisted for future achievements. */
    var lifetimeTaps: Long

    companion object {
        const val DEFAULT_ORE_PER_TAP = 1L
        const val DEFAULT_PASSIVE_RATE = 120L   // ore/hour ≈ 1 ore every 30 s
        const val PASSIVE_CAP_HOURS = 8L        // offline cap: 8 hours of accrual
    }
}

/**
 * [SharedPreferences]-backed implementation of [MiningPreferences].
 */
class SharedPrefsMiningPreferences(context: Context) : MiningPreferences {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override var totalOre: Long
        get() = prefs.getLong(KEY_TOTAL_ORE, 0L)
        set(value) = prefs.edit().putLong(KEY_TOTAL_ORE, value).apply()

    override var orePerTap: Long
        get() = prefs.getLong(KEY_ORE_PER_TAP, MiningPreferences.DEFAULT_ORE_PER_TAP)
        set(value) = prefs.edit().putLong(KEY_ORE_PER_TAP, value).apply()

    override var passiveOrePerHour: Long
        get() = prefs.getLong(KEY_PASSIVE_RATE, MiningPreferences.DEFAULT_PASSIVE_RATE)
        set(value) = prefs.edit().putLong(KEY_PASSIVE_RATE, value).apply()

    override var lastTimestampMillis: Long
        get() = prefs.getLong(KEY_LAST_TIMESTAMP, System.currentTimeMillis())
        set(value) = prefs.edit().putLong(KEY_LAST_TIMESTAMP, value).apply()

    override var lifetimeTaps: Long
        get() = prefs.getLong(KEY_LIFETIME_TAPS, 0L)
        set(value) = prefs.edit().putLong(KEY_LIFETIME_TAPS, value).apply()

    companion object {
        private const val PREFS_NAME = "civiltas_mining"
        private const val KEY_TOTAL_ORE = "total_ore"
        private const val KEY_ORE_PER_TAP = "ore_per_tap"
        private const val KEY_PASSIVE_RATE = "passive_rate"
        private const val KEY_LAST_TIMESTAMP = "last_timestamp"
        private const val KEY_LIFETIME_TAPS = "lifetime_taps"
    }
}
