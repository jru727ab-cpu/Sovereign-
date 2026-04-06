package com.civiltas.app.domain.model

enum class BuildingType(val displayName: String, val description: String) {
    MINE("Mine", "Extract raw ore from the earth"),
    OIL_DERRICK("Oil Derrick", "Pump crude oil from underground reserves"),
    LUMBER_CAMP("Lumber Camp", "Harvest sustainable timber resources"),
    FARM("Farm", "Grow crops to feed your population"),
    LIBRARY("Library", "Research ancient texts and new technologies"),
    VAULT("Vault", "Secure storage for your most precious resources"),
    WATCHTOWER("Watchtower", "Monitor for threats and incoming catastrophe"),
    SHRINE("Shrine", "Commune with the secret society for hidden knowledge"),
    BUNKER("Bunker", "Survive the next catastrophic event"),
    MARKET("Market", "Trade resources with other survivors")
}

data class Building(
    val id: Long = 0L,
    val type: BuildingType,
    val level: Int = 1,
    val isUnlocked: Boolean = false,
    val isConstructing: Boolean = false,
    val constructionCompleteAt: Long? = null
)
