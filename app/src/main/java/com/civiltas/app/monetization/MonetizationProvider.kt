package com.civiltas.app.monetization

/**
 * Top-level façade for all monetization features.
 *
 * Game code interacts with this interface only. The concrete implementation
 * (NoOpMonetizationProvider in MVP; wired providers in later phases) is supplied
 * via dependency injection so no game-logic changes are needed when billing is
 * activated.
 */
interface MonetizationProvider {

    /** Returns true if the player has purchased the permanent "Remove Ads" item. */
    val isAdsRemoved: Boolean

    /** Returns true if the player has an active VIP / Patron subscription. */
    val isVipActive: Boolean

    /** Returns true if the player has an active season pass for [seasonId]. */
    fun hasSeasonPass(seasonId: String): Boolean

    /** Access the ad-specific features. */
    val ads: AdProvider

    /** Access the in-app purchase features. */
    val iap: IapProvider

    /** Access the external payment features (card / crypto; APK distribution only). */
    val payments: PaymentProvider
}
