package com.civiltas.app.data

import android.content.Context
import android.content.SharedPreferences
import com.civiltas.app.data.model.UnlockSource

/**
 * Tracks earning milestones and triggers Secret unlocks when thresholds are crossed.
 *
 * MVP earning sources:
 *  - [DAILY_TASK_MILESTONE]: consecutive daily task completions
 *  - [EXPEDITION_MILESTONE]: total expeditions completed
 *
 * When a threshold is crossed, the repository unlocks the next available Secret for that source
 * and updates the forecast confidence via [ForecastRepository.recalculate].
 */
class EarningEngine(
    context: Context,
    private val secretsRepository: SecretsRepository,
    private val forecastRepository: ForecastRepository
) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    // ── Daily task streak ──────────────────────────────────────────────────────

    /** Records a completed daily task. Returns any Secret ID newly unlocked, or null. */
    fun recordDailyTask(): String? {
        val streak = prefs.getInt(KEY_TASK_STREAK, 0) + 1
        prefs.edit().putInt(KEY_TASK_STREAK, streak).apply()
        return checkMilestone(UnlockSource.DAILY_TASK_MILESTONE, streak, TASK_THRESHOLDS)
    }

    fun getDailyTaskStreak(): Int = prefs.getInt(KEY_TASK_STREAK, 0)

    // ── Expedition milestone ───────────────────────────────────────────────────

    /** Records a completed expedition. Returns any Secret ID newly unlocked, or null. */
    fun recordExpedition(): String? {
        val count = prefs.getInt(KEY_EXPEDITION_COUNT, 0) + 1
        prefs.edit().putInt(KEY_EXPEDITION_COUNT, count).apply()
        return checkMilestone(UnlockSource.EXPEDITION_MILESTONE, count, EXPEDITION_THRESHOLDS)
    }

    fun getExpeditionCount(): Int = prefs.getInt(KEY_EXPEDITION_COUNT, 0)

    // ── Internal ──────────────────────────────────────────────────────────────

    /**
     * Checks if [currentValue] has just hit a threshold. If so, finds the next locked Secret
     * for this [source], unlocks it, recalculates forecast confidence, and returns the secret id.
     */
    private fun checkMilestone(
        source: UnlockSource,
        currentValue: Int,
        thresholds: List<Int>
    ): String? {
        if (currentValue !in thresholds) return null

        // Find the next locked secret for this source in catalog order
        val candidate = SecretsCatalog.all
            .filter { it.unlockSource == source && !secretsRepository.isUnlocked(it.id) }
            .firstOrNull()
            ?: return null

        secretsRepository.unlock(candidate.id)
        forecastRepository.recalculate()
        return candidate.id
    }

    companion object {
        private const val PREFS_NAME = "civiltas_earning"
        private const val KEY_TASK_STREAK = "daily_task_streak"
        private const val KEY_EXPEDITION_COUNT = "expedition_count"

        /** Consecutive daily task completions that trigger a Secret unlock. */
        val TASK_THRESHOLDS = listOf(7, 14, 21, 30, 60)

        /** Expedition counts that trigger a Secret unlock. */
        val EXPEDITION_THRESHOLDS = listOf(3, 10, 25, 50, 100)
    }
}
