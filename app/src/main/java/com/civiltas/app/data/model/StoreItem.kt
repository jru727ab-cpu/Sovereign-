package com.civiltas.app.data.model

/**
 * A store catalogue item. Payment processing is stubbed — the [SkuType] and [priceUsd] are
 * descriptive only until a billing provider is wired in.
 *
 * Guardrail: every SKU contains lore, cosmetics, or time-saving convenience.
 * No SKU provides an exclusive permanent power advantage unavailable through free play.
 */
data class StoreItem(
    val skuId: String,
    val displayName: String,
    val description: String,
    val priceUsd: String,
    val skuType: SkuType,
    val secretIds: List<String>
)

enum class SkuType {
    ONE_TIME_PACK,
    SEASON_PASS,
    SUBSCRIPTION
}
