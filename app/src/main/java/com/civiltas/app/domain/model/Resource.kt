package com.civiltas.app.domain.model

enum class ResourceType(val displayName: String, val emoji: String) {
    IRON("Iron", "⚙️"),
    COPPER("Copper", "🔶"),
    GOLD("Gold", "🥇"),
    OIL("Oil", "🛢️"),
    COAL("Coal", "🪨"),
    STONE("Stone", "🪵"),
    LUMBER("Lumber", "🌲"),
    FOOD("Food", "🌾"),
    WATER("Water", "💧"),
    KNOWLEDGE("Knowledge", "📚"),
    ANCIENT_RELIC("Ancient Relic", "🏺"),
    GNOSIS("Gnosis", "✨")
}

data class Resource(
    val type: ResourceType,
    val amount: Double,
    val miningRatePerHour: Double
)
