package com.civiltas.app.data

data class MiningState(
    val oreBalance: Double = 0.0,
    val orePerSecond: Double = 0.1,
    val upgradeLevel: Int = 0,
    val lastSaveMs: Long = 0L
)

data class SecretEntry(
    val id: String,
    val title: String,
    val category: SecretCategory,
    val description: String,
    val hint: String,
    val effect: String,
    val isUnlocked: Boolean = false,
    val earnSource: EarnSource
)

enum class SecretCategory(val displayName: String) {
    LORE("Lore"),
    SURVIVAL_INTEL("Survival"),
    RESOURCE_INTEL("Resources"),
    SACRED_GEOMETRY("Sacred Geometry")
}

enum class EarnSource(val displayName: String) {
    MILESTONE("Milestone"),
    EXPEDITION("Expedition"),
    RESEARCH("Research"),
    PURCHASE("Purchase")
}

data class GameState(
    val miningState: MiningState = MiningState(),
    val unlockedSecretIds: Set<String> = emptySet()
)
