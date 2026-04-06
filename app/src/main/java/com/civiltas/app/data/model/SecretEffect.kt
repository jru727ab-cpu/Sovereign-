package com.civiltas.app.data.model

/**
 * The mechanical effect a Secret applies when unlocked.
 *
 * [FORECAST_CONFIDENCE] adds points to the forecast confidence meter.
 * [RESOURCE_EFFICIENCY] improves resource yield/cost by a percentage.
 * [BLUEPRINT_UNLOCK] unlocks a building type identified by [unlockKey].
 * [EXPEDITION_BONUS] improves expedition success rate / loot.
 * [MORALE_BOOST] increases population morale → task speed.
 * [OFFLINE_BONUS] improves resources earned while the app is closed.
 * [LORE_ONLY] no mechanical effect; purely narrative.
 */
sealed class SecretEffect {
    data class ForecastConfidence(val points: Int) : SecretEffect()
    data class ResourceEfficiency(val percentBonus: Int) : SecretEffect()
    data class BlueprintUnlock(val unlockKey: String, val buildingName: String) : SecretEffect()
    data class ExpeditionBonus(val percentBonus: Int) : SecretEffect()
    data class MoraleBoost(val percentBonus: Int) : SecretEffect()
    data class OfflineBonus(val percentBonus: Int) : SecretEffect()
    object LoreOnly : SecretEffect()
}
