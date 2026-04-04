package com.civiltas.secrets

// ---------------------------------------------------------------------------
// Data classes — mirrors data/secrets.json
// ---------------------------------------------------------------------------

enum class SecretCategory { LORE, SURVIVAL_INTEL, RESOURCE_INTEL, SACRED_GEOMETRY, SOCIETY_RANKS }

enum class SecretTier(val label: String) {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary")
}

enum class EarnPath {
    DAILY_TASK,
    QUEST,
    EXPEDITION,
    LONG_EXPEDITION,
    RESEARCH_MILESTONE,
    SEASON_PASS_FREE,
    SEASON_PASS_PREMIUM,
    CATASTROPHE_CYCLE_COMPLETION
}

enum class PurchaseType {
    NONE,
    CONVENIENCE,       // same secret, just faster — never extra power
    EARLY_ACCESS,      // skip unlock delay
    LORE_PACK,         // narrative/cosmetic bundle
    COSMETIC_COLLECTOR // collector's item, zero gameplay impact
}

data class SecretEffect(
    val type: String,           // "unlock_blueprint", "production_rate_bonus", etc.
    val value: String? = null,  // referenced ID or route key
    val bonusPct: Int = 0       // percentage modifier where applicable
)

data class Secret(
    val id: String,
    val tier: SecretTier,
    val category: SecretCategory,
    val title: String,
    val hint: String,
    val description: String,
    val effect: SecretEffect,
    val earnPath: EarnPath,
    val purchasable: Boolean,
    val purchaseType: PurchaseType,
    val unlockDelayHours: Int
)

// ---------------------------------------------------------------------------
// MVP catalogue — matches data/secrets.json
// ---------------------------------------------------------------------------

object SecretsData {

    val all: List<Secret> = listOf(
        Secret(
            id = "SEC_001",
            tier = SecretTier.COMMON,
            category = SecretCategory.LORE,
            title = "The First Signal",
            hint = "A fragmented transmission recorded 72 hours before the event.",
            description = "A corrupted radio log recovered from a buried relay station. Someone was warned — and chose silence.",
            effect = SecretEffect(type = "unlock_codex", value = "codex_event_origin"),
            earnPath = EarnPath.DAILY_TASK,
            purchasable = false,
            purchaseType = PurchaseType.NONE,
            unlockDelayHours = 0
        ),
        Secret(
            id = "SEC_002",
            tier = SecretTier.COMMON,
            category = SecretCategory.RESOURCE_INTEL,
            title = "The Eastern Seam",
            hint = "An old miner's note about deposits that 'shouldn't exist'.",
            description = "A handwritten map fragment pointing to a mineral-rich seam buried beneath the Eastern Wastes.",
            effect = SecretEffect(type = "reveal_deposit", value = "eastern_ore_seam", bonusPct = 20),
            earnPath = EarnPath.QUEST,
            purchasable = false,
            purchaseType = PurchaseType.NONE,
            unlockDelayHours = 0
        ),
        Secret(
            id = "SEC_003",
            tier = SecretTier.UNCOMMON,
            category = SecretCategory.SURVIVAL_INTEL,
            title = "The Green Corridor",
            hint = "A migration path used by those who made it out.",
            description = "A route with measurably lower radiation. Movement speed along this corridor is increased.",
            effect = SecretEffect(type = "expedition_speed_bonus", value = "green_corridor_route", bonusPct = 25),
            earnPath = EarnPath.EXPEDITION,
            purchasable = false,
            purchaseType = PurchaseType.NONE,
            unlockDelayHours = 4
        ),
        Secret(
            id = "SEC_004",
            tier = SecretTier.UNCOMMON,
            category = SecretCategory.RESOURCE_INTEL,
            title = "Cold Extraction",
            hint = "A method that extracts ore without heat — and without detection.",
            description = "A pre-catastrophe technique that reduces energy cost for ore extraction by 15%.",
            effect = SecretEffect(type = "production_rate_bonus", value = "ore", bonusPct = 15),
            earnPath = EarnPath.RESEARCH_MILESTONE,
            purchasable = false,
            purchaseType = PurchaseType.NONE,
            unlockDelayHours = 8
        ),
        Secret(
            id = "SEC_005",
            tier = SecretTier.RARE,
            category = SecretCategory.LORE,
            title = "The Architect's Confession",
            hint = "Someone built the machine that ended everything — and wrote it down.",
            description = "A sealed letter found in a lead-lined vault describing — in clinical detail — the device constructed to reset civilisation.",
            effect = SecretEffect(type = "unlock_story_chapter", value = "chapter_architect"),
            earnPath = EarnPath.SEASON_PASS_FREE,
            purchasable = true,
            purchaseType = PurchaseType.CONVENIENCE,
            unlockDelayHours = 24
        ),
        Secret(
            id = "SEC_006",
            tier = SecretTier.RARE,
            category = SecretCategory.SACRED_GEOMETRY,
            title = "The Hexagonal Principle",
            hint = "Structures built on this pattern are still standing.",
            description = "Ancient architectural mathematics. Buildings using this principle resist catastrophe damage.",
            effect = SecretEffect(type = "building_resilience_bonus", bonusPct = 30),
            earnPath = EarnPath.RESEARCH_MILESTONE,
            purchasable = true,
            purchaseType = PurchaseType.CONVENIENCE,
            unlockDelayHours = 24
        ),
        Secret(
            id = "SEC_007",
            tier = SecretTier.RARE,
            category = SecretCategory.SURVIVAL_INTEL,
            title = "Subsurface Cache Network",
            hint = "They buried supplies in places that couldn't burn.",
            description = "A network of underground caches. Resource retention during catastrophe events increased by 20%.",
            effect = SecretEffect(type = "reveal_cache_network", bonusPct = 20),
            earnPath = EarnPath.EXPEDITION,
            purchasable = true,
            purchaseType = PurchaseType.CONVENIENCE,
            unlockDelayHours = 24
        ),
        Secret(
            id = "SEC_008",
            tier = SecretTier.EPIC,
            category = SecretCategory.SACRED_GEOMETRY,
            title = "Geomantic Anchor",
            hint = "Buildings tied to ley lines survived what nothing else did.",
            description = "A complete blueprint for a geomantic foundation system. Near-total catastrophe resistance.",
            effect = SecretEffect(type = "unlock_blueprint", value = "blueprint_geomantic_foundation"),
            earnPath = EarnPath.LONG_EXPEDITION,
            purchasable = true,
            purchaseType = PurchaseType.EARLY_ACCESS,
            unlockDelayHours = 48
        ),
        Secret(
            id = "SEC_009",
            tier = SecretTier.EPIC,
            category = SecretCategory.SOCIETY_RANKS,
            title = "The Third Seal",
            hint = "A symbol those who held real power would recognise.",
            description = "The rank insignia of the Third Circle. Unlocks faction quests and elevated NPC trust.",
            effect = SecretEffect(type = "faction_standing_bonus", value = "old_order", bonusPct = 50),
            earnPath = EarnPath.SEASON_PASS_PREMIUM,
            purchasable = true,
            purchaseType = PurchaseType.LORE_PACK,
            unlockDelayHours = 48
        ),
        Secret(
            id = "SEC_010",
            tier = SecretTier.LEGENDARY,
            category = SecretCategory.LORE,
            title = "The Complete Record",
            hint = "One person documented everything. They did not survive.",
            description = "The full account of events leading to the catastrophe. Changes the ending of the current cycle.",
            effect = SecretEffect(type = "unlock_ending", value = "ending_truth_revealed"),
            earnPath = EarnPath.CATASTROPHE_CYCLE_COMPLETION,
            purchasable = true,
            purchaseType = PurchaseType.COSMETIC_COLLECTOR,
            unlockDelayHours = 0
        )
    )

    fun byId(id: String): Secret? = all.firstOrNull { it.id == id }
    fun byCategory(category: SecretCategory): List<Secret> = all.filter { it.category == category }
    fun byTier(tier: SecretTier): List<Secret> = all.filter { it.tier == tier }
}
