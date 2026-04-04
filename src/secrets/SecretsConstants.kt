package com.civiltas.app.secrets

/**
 * UI string constants for the Order of the Compass — Secrets Library screens.
 *
 * All copy referencing the secret society uses the confirmed name
 * "Order of the Compass". Prefer these constants in Composables and
 * ViewModels instead of inline string literals so the copy stays
 * consistent and easy to update.
 */
object SecretsConstants {

    // ── Society name ────────────────────────────────────────────────────────
    const val SOCIETY_NAME = "Order of the Compass"
    const val SOCIETY_TAGLINE = "Every line drawn by a compass traces the boundary between what is known and what must never be forgotten."

    // ── Screen titles ────────────────────────────────────────────────────────
    const val SCREEN_SECRETS_LIBRARY = "Secrets Library"
    const val SCREEN_SECRET_DETAIL   = "Order Teaching"
    const val SCREEN_ORDER_PROFILE   = "Compass Rank"

    // ── Secrets Library — empty / locked states ──────────────────────────────
    const val LIBRARY_EMPTY_TITLE    = "No Teachings Collected"
    const val LIBRARY_EMPTY_BODY     = "Complete quests and expeditions to recover Order of the Compass teachings."
    const val LIBRARY_LOCKED_LABEL   = "Restricted — Raise your Compass Rank to unlock"

    // ── Secret card labels ───────────────────────────────────────────────────
    const val CARD_TYPE_TEACHING     = "Teaching"
    const val CARD_TYPE_DOSSIER      = "Dossier"
    const val CARD_TYPE_BLUEPRINT    = "Blueprint"
    const val CARD_TYPE_FRAGMENT     = "Fragment"
    const val CARD_LOCKED_HINT       = "Earn this from: %s"   // %s = source (quest / expedition / IAP)
    const val CARD_NEW_BADGE         = "New"

    // ── Compass Rank labels ──────────────────────────────────────────────────
    const val RANK_0 = "Seeker"
    const val RANK_1 = "Initiate"
    const val RANK_2 = "Candidate"
    const val RANK_3 = "Adept"
    const val RANK_4 = "Keeper"
    const val RANK_5 = "Guardian"
    const val RANK_6 = "Archon"

    const val RANK_LABEL_PREFIX = "Compass Rank:"
    const val RANK_PROGRESS_FORMAT = "%d / %d Secrets"   // collected / required for next rank
    const val RANK_MAX_LABEL       = "Archon — Complete Compass"

    // ── Gnosis ───────────────────────────────────────────────────────────────
    const val GNOSIS_LABEL         = "Gnosis"
    const val GNOSIS_DESCRIPTION   = "Your accumulated Order knowledge. Increased by collecting Secrets and completing Order quests."

    // ── Forecast Meter ───────────────────────────────────────────────────────
    const val FORECAST_LABEL       = "Catastrophe Forecast"
    const val FORECAST_UNKNOWN     = "Unknown — Collect Order Dossiers to increase confidence"
    const val FORECAST_FORMAT      = "%d %% confidence"
    const val FORECAST_FULL        = "Forecast resolved — consult the Archon's Mandate"

    // ── Sacred Geometry ──────────────────────────────────────────────────────
    const val GEOMETRY_SECTION_TITLE = "Sacred Geometry"
    const val GEOMETRY_UNLOCKED_MSG  = "Blueprint unlocked: %s"   // %s = blueprint name
    const val GEOMETRY_LOCKED_MSG    = "Reach Adept rank to access Sacred Geometry blueprints"

    // ── Obtain / acquisition strings ─────────────────────────────────────────
    const val OBTAIN_QUEST          = "Order Quest"
    const val OBTAIN_EXPEDITION     = "Expedition"
    const val OBTAIN_RESEARCH       = "Research Milestone"
    const val OBTAIN_DAILY_TASK     = "Daily Order Task"
    const val OBTAIN_IAP            = "Secrets Pack"

    // ── Store / IAP ──────────────────────────────────────────────────────────
    const val STORE_SECTION_TITLE    = "Order Archives Store"
    const val STORE_PACK_LORE        = "Catastrophe Dossiers — Chapter Bundle"
    const val STORE_PACK_GEOMETRY    = "Sacred Geometry Collection"
    const val STORE_PACK_INTEL       = "Survival Intel Dossiers"
    const val STORE_PACK_STARTER     = "Seeker's Starter Pack"
    const val STORE_CTA_UNLOCK       = "Unlock from Order Archives"
    const val STORE_OWNED_LABEL      = "Collected"

    // ── Chapter names ────────────────────────────────────────────────────────
    const val CHAPTER_1 = "Chapter I — Orientation"
    const val CHAPTER_2 = "Chapter II — Survival Intel"
    const val CHAPTER_3 = "Chapter III — Resource Strata"
    const val CHAPTER_4 = "Chapter IV — Sacred Geometry"
    const val CHAPTER_5 = "Chapter V — Catastrophe Dossiers"
    const val CHAPTER_6 = "Chapter VI — The Second Signal"

    // ── Onboarding / first-run ────────────────────────────────────────────────
    const val ONBOARDING_TITLE  = "You Have Been Found by the Order"
    const val ONBOARDING_BODY   = "The Order of the Compass preserved what the world forgot. Collect their teachings, raise your Compass Rank, and prepare for what is coming."
    const val ONBOARDING_CTA    = "Enter the Secrets Library"
}
