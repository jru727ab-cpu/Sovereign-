package com.civiltas.app.data.model

/**
 * Categories of Secrets reflecting the two pillars of the secret society:
 * - Directorate of Archivists (grounded, analytical, sci-fi)
 * - Order of the Eternal Compass (esoteric, geometric, mystical)
 * Plus cross-pillar survivor testimonies.
 */
enum class SecretCategory(val displayName: String, val pillar: String) {
    DIRECTORATE_INTEL(
        displayName = "Directorate Intel",
        pillar = "Archivists"
    ),
    CONTINUITY_PROTOCOL(
        displayName = "Continuity Protocol",
        pillar = "Archivists"
    ),
    ORDER_GEOMETRY(
        displayName = "Sacred Geometry",
        pillar = "Order of the Eternal Compass"
    ),
    SURVIVOR_TESTIMONY(
        displayName = "Survivor Testimony",
        pillar = "Cross-Pillar"
    )
}
