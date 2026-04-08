package com.civiltas.app.monetization

/**
 * Rewarded-ad provider interface.
 *
 * All ads in CIVILTAS are opt-in/rewarded. Interstitials and banner ads are
 * explicitly prohibited (see docs/MONETIZATION.md § Guardrails G-1).
 *
 * Phase 2: implement with AdMob / Unity Ads / IronSource.
 */
interface AdProvider {

    /**
     * True if a rewarded ad is loaded and ready to display.
     * Always false in the no-op implementation.
     */
    val isRewardedAdReady: Boolean

    /**
     * Request the provider to pre-load the next rewarded ad.
     * Safe to call frequently; the provider debounces internally.
     */
    fun preloadRewardedAd()

    /**
     * Show a rewarded ad.
     *
     * @param placement  A stable key identifying the UI context (e.g. "double_idle",
     *                   "instant_finish", "expedition_slot", "streak_shield").
     * @param onRewarded Called on the main thread when the player has earned the reward.
     *                   Not called if the player closes early or no ad is available.
     * @param onFailed   Called on the main thread if the ad could not be shown.
     */
    fun showRewardedAd(
        placement: String,
        onRewarded: () -> Unit,
        onFailed: (reason: AdFailureReason) -> Unit,
    )
}

/** Reason codes returned when a rewarded ad cannot be shown. */
enum class AdFailureReason {
    /** No ad is loaded yet. */
    NOT_READY,

    /** The player dismissed the ad before the reward threshold. */
    DISMISSED_EARLY,

    /** Ads have been permanently removed via IAP. */
    ADS_REMOVED,

    /** Network or SDK-level error. */
    PROVIDER_ERROR,
}
