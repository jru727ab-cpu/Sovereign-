package com.civiltas.app.data

import android.content.Context
import android.content.SharedPreferences
import com.civiltas.app.data.model.SecretEffect

/**
 * Tracks the player's Forecast Confidence score (0–100).
 *
 * Confidence is accumulated by unlocking Secrets whose effect is [SecretEffect.ForecastConfidence].
 * The score is persisted in SharedPreferences and recalculated from the unlocked secrets set.
 */
class ForecastRepository(
    context: Context,
    private val secretsRepository: SecretsRepository
) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Recalculates and persists forecast confidence based on currently unlocked secrets.
     * Returns the updated score (capped at [MAX_CONFIDENCE]).
     */
    fun recalculate(): Int {
        val total = secretsRepository.unlockedIds()
            .mapNotNull { id -> SecretsCatalog.findById(id) }
            .sumOf { secret ->
                when (val effect = secret.effect) {
                    is SecretEffect.ForecastConfidence -> effect.points
                    else -> 0
                }
            }
        val capped = minOf(total, MAX_CONFIDENCE)
        prefs.edit().putInt(KEY_CONFIDENCE, capped).apply()
        return capped
    }

    /** Returns the last persisted forecast confidence score. */
    fun getConfidence(): Int = prefs.getInt(KEY_CONFIDENCE, 0)

    /**
     * Returns a human-readable arrival window string based on current confidence.
     *
     * Confidence   Window
     * 0–29         18–180 days
     * 30–59        18–90 days
     * 60–79        18–45 days
     * 80–99        18–28 days
     * 100          Exact window known
     */
    fun getArrivalWindow(): String = when (getConfidence()) {
        in 0..29 -> "~18–180 days"
        in 30..59 -> "~18–90 days"
        in 60..79 -> "~18–45 days"
        in 80..99 -> "~18–28 days"
        else -> "Exact window confirmed"
    }

    companion object {
        private const val PREFS_NAME = "civiltas_forecast"
        private const val KEY_CONFIDENCE = "forecast_confidence"
        const val MAX_CONFIDENCE = 100
    }
}
