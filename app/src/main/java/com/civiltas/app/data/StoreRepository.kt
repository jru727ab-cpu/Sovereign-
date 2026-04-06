package com.civiltas.app.data

import com.civiltas.app.data.model.SkuType
import com.civiltas.app.data.model.StoreItem

/**
 * The store catalogue for CIVILTAS.
 *
 * Payment processing is **stubbed** in the MVP. The [StoreRepository] exposes the catalogue
 * and a [initiatePurchase] hook. Wire a real billing provider (Google Play Billing for Play Store
 * distribution; Stripe or crypto for direct APK distribution) without changing game logic.
 *
 * Guardrail: every SKU contains lore, story depth, convenience, or cosmetics.
 * No SKU provides an exclusive permanent power advantage gated behind payment only.
 */
object StoreRepository {

    val catalogue: List<StoreItem> = listOf(
        StoreItem(
            skuId = "secrets_pack_archivist_common",
            displayName = "Archivist Dossier: Common",
            description = "3 common Directorate Intel secrets — cold-case files, " +
                "field surveys, and fragmented testimony from the Archivists' vaults.",
            priceUsd = "1.99",
            skuType = SkuType.ONE_TIME_PACK,
            secretIds = listOf("arc_001", "arc_002", "arc_003")
        ),
        StoreItem(
            skuId = "secrets_pack_order_common",
            displayName = "Order Codex: Common",
            description = "3 common Order of the Eternal Compass secrets — initiates' " +
                "geometry diagrams and the first sacred glyphs.",
            priceUsd = "1.99",
            skuType = SkuType.ONE_TIME_PACK,
            secretIds = listOf("ord_001", "ord_002")
        ),
        StoreItem(
            skuId = "secrets_pack_intel_vol1",
            displayName = "Archivist Intel Vol. I",
            description = "2 Uncommon + 1 Rare Directorate secrets including Evacuation " +
                "Protocol Delta-7, the Asset Recovery Codex, and the Collapse Chronology.",
            priceUsd = "3.99",
            skuType = SkuType.ONE_TIME_PACK,
            secretIds = listOf("arc_004", "arc_005", "arc_006", "arc_008", "sur_001")
        ),
        StoreItem(
            skuId = "secrets_pack_geometry_vol1",
            displayName = "Sacred Geometry Vol. I",
            description = "2 Uncommon + 1 Rare Order secrets — blueprint unlocks for the " +
                "Reinforced Shelter and the legendary Resonance Forge.",
            priceUsd = "3.99",
            skuType = SkuType.ONE_TIME_PACK,
            secretIds = listOf("ord_003", "ord_004")
        ),
        StoreItem(
            skuId = "secrets_pack_classified_bundle",
            displayName = "Classified Bundle",
            description = "The Directorate's deepest secrets: The Initiating Event, " +
                "The Last Broadcast, and Order 0 — who started it all.",
            priceUsd = "5.99",
            skuType = SkuType.ONE_TIME_PACK,
            secretIds = listOf("arc_007", "sur_002", "arc_009")
        ),
        StoreItem(
            skuId = "season_pass",
            displayName = "Catastrophe Cycle Season Pass",
            description = "Premium track for the current catastrophe cycle. Includes " +
                "all-tier secrets, exclusive cosmetic rewards, early cycle intel, " +
                "and a special Contingency action during the Impact Phase.",
            priceUsd = "9.99",
            skuType = SkuType.SEASON_PASS,
            secretIds = SecretsCatalog.all.filter { it.storeSkuId != null }.map { it.id }
        )
    )

    /**
     * Stub for initiating a purchase flow. Replace with a real billing provider implementation.
     *
     * @param skuId        The SKU to purchase.
     * @param onSuccess    Called with the purchased SKU id if the transaction succeeds.
     * @param onError      Called with an error message if the transaction fails.
     */
    fun initiatePurchase(
        skuId: String,
        onSuccess: (skuId: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        // STUB: In production, call Google Play Billing / Stripe / etc.
        onError("Payment processing not yet available. Earn secrets through gameplay!")
    }

    /** Looks up a catalogue item by SKU id. */
    fun findBySku(skuId: String): StoreItem? = catalogue.firstOrNull { it.skuId == skuId }
}
