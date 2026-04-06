package com.civiltas.app.data.model

/** How a Secret can be earned without spending money. */
enum class UnlockSource(val displayName: String) {
    DAILY_TASK_MILESTONE(displayName = "7-Day Task Streak"),
    EXPEDITION_MILESTONE(displayName = "Expedition Milestone"),
    GNOSIS_RESEARCH(displayName = "Gnosis Research"),
    SOCIETY_RANK(displayName = "Society Rank"),
    SEASON_PASS(displayName = "Season Pass (Premium Track)")
}

/**
 * A single collectible knowledge artefact.
 *
 * @param id            Unique identifier, e.g. "arc_001".
 * @param title         Short display title.
 * @param category      Which pillar/faction this belongs to.
 * @param tier          Rarity tier controlling effect magnitude and unlock difficulty.
 * @param lore          2–4 sentence flavour text displayed in the detail view.
 * @param effectDescription Human-readable effect summary for the UI.
 * @param effect        Sealed class representing the mechanical effect.
 * @param unlockSource  The free earnable path to obtain this secret.
 * @param storeSkuId    Store SKU this secret is included in, or null if earn-only.
 */
data class Secret(
    val id: String,
    val title: String,
    val category: SecretCategory,
    val tier: SecretTier,
    val lore: String,
    val effectDescription: String,
    val effect: SecretEffect,
    val unlockSource: UnlockSource,
    val storeSkuId: String? = null
)
