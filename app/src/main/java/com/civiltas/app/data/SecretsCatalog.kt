package com.civiltas.app.data

import com.civiltas.app.data.model.Secret
import com.civiltas.app.data.model.SecretCategory.*
import com.civiltas.app.data.model.SecretEffect.*
import com.civiltas.app.data.model.SecretTier.*
import com.civiltas.app.data.model.UnlockSource.*

/**
 * The built-in Secrets catalogue for CIVILTAS v1.
 *
 * 15 secrets across both pillars of the secret society:
 *   - Directorate of Archivists (DIRECTORATE_INTEL, CONTINUITY_PROTOCOL)
 *   - Order of the Eternal Compass (ORDER_GEOMETRY)
 *   - Cross-pillar SURVIVOR_TESTIMONY
 *
 * 5 secrets are linked to FORECAST_CONFIDENCE and drive the Forecast Meter.
 * Design guardrail: every secret has at least one free earnable path.
 */
object SecretsCatalog {

    val all: List<Secret> = listOf(

        // ── COMMON ─────────────────────────────────────────────────────────────

        Secret(
            id = "arc_001",
            title = "Zone 7 Contamination Survey",
            category = DIRECTORATE_INTEL,
            tier = COMMON,
            lore = "A redacted field assessment compiled eighteen months before the Pulse. " +
                "Zone 7 showed elevated seismic micro-fractures consistent with induced " +
                "resonance. The analyst who filed this report retired two weeks later. " +
                "No forwarding address was ever logged.",
            effectDescription = "Forecast Confidence +5",
            effect = ForecastConfidence(points = 5),
            unlockSource = DAILY_TASK_MILESTONE,
            storeSkuId = "secrets_pack_archivist_common"
        ),

        Secret(
            id = "arc_002",
            title = "Pre-Collapse Population Density Map",
            category = DIRECTORATE_INTEL,
            tier = COMMON,
            lore = "A topographical overlay of settlement density, transport routes, and " +
                "resource extraction nodes from Cycle 6's peak. Dense nodes were the first " +
                "to fall. The hinterlands lasted longer — not because they were safer, " +
                "but because no one remembered they existed.",
            effectDescription = "Expedition success rate +3%",
            effect = ExpeditionBonus(percentBonus = 3),
            unlockSource = EXPEDITION_MILESTONE,
            storeSkuId = "secrets_pack_archivist_common"
        ),

        Secret(
            id = "arc_003",
            title = "The Pulse Anomaly (Fragmented Log)",
            category = SURVIVOR_TESTIMONY,
            tier = COMMON,
            lore = "Recovered from a personal device found in Sector 12. The author's name " +
                "is unreadable. \"Day 1: the lights went out differently this time — not a " +
                "flicker, a silence. Day 2: still no signal. Day 3: I understand now that " +
                "I am among the ones expected to not make it.\"",
            effectDescription = "Lore reveal — no mechanical effect",
            effect = LoreOnly,
            unlockSource = DAILY_TASK_MILESTONE,
            storeSkuId = "secrets_pack_archivist_common"
        ),

        Secret(
            id = "ord_001",
            title = "The Fibonacci Stockpile Arrangement",
            category = ORDER_GEOMETRY,
            tier = COMMON,
            lore = "The Order has known for centuries that certain spatial arrangements " +
                "of stored materials reduce spoilage, contamination, and what they call " +
                "\"entropic drift.\" Whether the effect is real or psychological, the data " +
                "does not care — yield is yield.",
            effectDescription = "Resource efficiency +2%",
            effect = ResourceEfficiency(percentBonus = 2),
            unlockSource = GNOSIS_RESEARCH,
            storeSkuId = "secrets_pack_order_common"
        ),

        Secret(
            id = "ord_002",
            title = "First Glyph: The Anchor",
            category = ORDER_GEOMETRY,
            tier = COMMON,
            lore = "The first glyph taught to initiates of the Order. An interlocking " +
                "triad inscribed at the foundation of any structure the Order deems worth " +
                "preserving. Its presence is a statement: this place intends to outlast " +
                "what is coming.",
            effectDescription = "Lore reveal — initiates' introduction to Order symbols",
            effect = LoreOnly,
            unlockSource = GNOSIS_RESEARCH,
            storeSkuId = "secrets_pack_order_common"
        ),

        // ── UNCOMMON ──────────────────────────────────────────────────────────

        Secret(
            id = "arc_004",
            title = "Evacuation Protocol Delta-7",
            category = CONTINUITY_PROTOCOL,
            tier = UNCOMMON,
            lore = "A pre-collapse contingency document from the Directorate's Continuity " +
                "Division. Delta-7 outlined a staged withdrawal from high-density zones " +
                "into designated resilience nodes. It was never officially activated. " +
                "Evidence suggests it was activated anyway, quietly, for select personnel.",
            effectDescription = "Offline resource accumulation +5%",
            effect = OfflineBonus(percentBonus = 5),
            unlockSource = EXPEDITION_MILESTONE,
            storeSkuId = "secrets_pack_intel_vol1"
        ),

        Secret(
            id = "arc_005",
            title = "Asset Recovery Codex, Vol. I",
            category = DIRECTORATE_INTEL,
            tier = UNCOMMON,
            lore = "A field manual for Directorate recovery operatives. Volume I covers " +
                "extraction priority, triage of salvageable infrastructure, and the " +
                "identification of resource caches hidden by the previous administration. " +
                "Volume II has not been found.",
            effectDescription = "Resource efficiency +5%",
            effect = ResourceEfficiency(percentBonus = 5),
            unlockSource = EXPEDITION_MILESTONE,
            storeSkuId = "secrets_pack_intel_vol1"
        ),

        Secret(
            id = "arc_006",
            title = "Collapse Chronology: First 72 Hours",
            category = DIRECTORATE_INTEL,
            tier = UNCOMMON,
            lore = "A reconstructed hour-by-hour account of the initial collapse event, " +
                "assembled from 847 fragmented data sources. The Directorate considers " +
                "this document Level 3 Sensitive. Understanding the sequence is the first " +
                "step to recognising it when it begins again.",
            effectDescription = "Forecast Confidence +10",
            effect = ForecastConfidence(points = 10),
            unlockSource = EXPEDITION_MILESTONE,
            storeSkuId = "secrets_pack_intel_vol1"
        ),

        Secret(
            id = "ord_003",
            title = "Sacred Geometry: The Resilient Arch",
            category = ORDER_GEOMETRY,
            tier = UNCOMMON,
            lore = "An architectural diagram handed down through seventeen generations of " +
                "the Order. The proportions are mathematically unusual — not Fibonacci, " +
                "not phi. The Order has no name for the ratio. Structures built to this " +
                "specification survive impacts that should destroy them.",
            effectDescription = "Unlocks blueprint: Reinforced Shelter",
            effect = BlueprintUnlock(
                unlockKey = "blueprint_reinforced_shelter",
                buildingName = "Reinforced Shelter"
            ),
            unlockSource = GNOSIS_RESEARCH,
            storeSkuId = "secrets_pack_geometry_vol1"
        ),

        Secret(
            id = "sur_001",
            title = "Testimony of Archivist Vael",
            category = SURVIVOR_TESTIMONY,
            tier = UNCOMMON,
            lore = "Archivist Vael survived Cycle 6 in a sub-basement of the Directorate " +
                "library complex. Her oral account, recorded three years post-collapse, " +
                "describes both the data she preserved and the geometry she began to see " +
                "in the structural damage patterns around her. She later joined the Order.",
            effectDescription = "Population morale +5%",
            effect = MoraleBoost(percentBonus = 5),
            unlockSource = SOCIETY_RANK,
            storeSkuId = "secrets_pack_intel_vol1"
        ),

        // ── RARE ──────────────────────────────────────────────────────────────

        Secret(
            id = "arc_007",
            title = "Classified: The Initiating Event",
            category = DIRECTORATE_INTEL,
            tier = RARE,
            lore = "This file contains the Directorate's closest estimate of the event that " +
                "triggered Cycle 6's collapse. The technical cause is documented. The " +
                "question of whether it was accidental has been redacted at the highest " +
                "level. Seven copies exist. This is the eighth.",
            effectDescription = "Forecast Confidence +20",
            effect = ForecastConfidence(points = 20),
            unlockSource = EXPEDITION_MILESTONE,
            storeSkuId = "secrets_pack_classified_bundle"
        ),

        Secret(
            id = "arc_008",
            title = "The Secondary Wave Model",
            category = CONTINUITY_PROTOCOL,
            tier = RARE,
            lore = "A predictive model for the secondary effects of a repeat collapse event: " +
                "infrastructure cascade timelines, population displacement vectors, and " +
                "the critical 48-hour window in which intervention remains possible. " +
                "This model was built by people who did not survive long enough to use it.",
            effectDescription = "Forecast Confidence +15",
            effect = ForecastConfidence(points = 15),
            unlockSource = SOCIETY_RANK,
            storeSkuId = "secrets_pack_intel_vol1"
        ),

        Secret(
            id = "ord_004",
            title = "The Harmonic Lattice Blueprint",
            category = ORDER_GEOMETRY,
            tier = RARE,
            lore = "The Order's most advanced architectural diagram available to non-initiated " +
                "members. The Harmonic Lattice describes a production facility layout derived " +
                "from interference patterns in resonant stone. Facilities built on this " +
                "design have never been found in post-collapse rubble.",
            effectDescription = "Unlocks blueprint: Resonance Forge",
            effect = BlueprintUnlock(
                unlockKey = "blueprint_resonance_forge",
                buildingName = "Resonance Forge"
            ),
            unlockSource = GNOSIS_RESEARCH,
            storeSkuId = "secrets_pack_geometry_vol1"
        ),

        Secret(
            id = "sur_002",
            title = "The Last Broadcast (Reconstructed)",
            category = SURVIVOR_TESTIMONY,
            tier = RARE,
            lore = "A partial reconstruction of the final emergency broadcast before " +
                "communications went dark in Cycle 6. The broadcast was interrupted " +
                "after forty-two seconds. The reconstructed final sentence reads: " +
                "\"It is not a natural event. It never was. Tell the—\"",
            effectDescription = "Population morale +10% — lore reveal",
            effect = MoraleBoost(percentBonus = 10),
            unlockSource = EXPEDITION_MILESTONE,
            storeSkuId = "secrets_pack_classified_bundle"
        ),

        // ── CLASSIFIED ────────────────────────────────────────────────────────

        Secret(
            id = "arc_009",
            title = "Order 0: Who Started It",
            category = DIRECTORATE_INTEL,
            tier = CLASSIFIED,
            lore = "The Directorate's final sealed document. Order 0 names the individuals " +
                "and the apparatus responsible for initiating Cycle 6. It also contains " +
                "the earliest confirmed evidence that a repeat event is not a risk to be " +
                "modelled — it is a schedule to be read. The next cycle is not coming. " +
                "It has already been set in motion.",
            effectDescription = "Forecast Confidence +30 — full lore reveal",
            effect = ForecastConfidence(points = 30),
            unlockSource = SEASON_PASS,
            storeSkuId = "secrets_pack_classified_bundle"
        )
    )

    /** Returns the Secret with the given id, or null if not found. */
    fun findById(id: String): Secret? = all.firstOrNull { it.id == id }

    /** Returns all secrets that contribute to forecast confidence. */
    val forecastSecrets: List<Secret>
        get() = all.filter { it.effect is ForecastConfidence }
}
