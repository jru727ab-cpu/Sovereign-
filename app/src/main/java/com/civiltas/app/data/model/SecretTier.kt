package com.civiltas.app.data.model

/** Rarity/depth tiers for Secrets. Higher tiers have stronger effects and harder unlock requirements. */
enum class SecretTier(val displayName: String, val sortOrder: Int) {
    COMMON(displayName = "Common", sortOrder = 0),
    UNCOMMON(displayName = "Uncommon", sortOrder = 1),
    RARE(displayName = "Rare", sortOrder = 2),
    CLASSIFIED(displayName = "Classified", sortOrder = 3)
}
