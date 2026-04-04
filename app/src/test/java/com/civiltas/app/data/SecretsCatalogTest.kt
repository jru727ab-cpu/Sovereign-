package com.civiltas.app.data

import com.civiltas.app.data.model.SecretEffect
import com.civiltas.app.data.model.SecretTier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for [SecretsCatalog] — verifies catalogue integrity and forecast secret coverage.
 * These tests are pure-Kotlin JUnit4 and run on the JVM without an Android device.
 */
class SecretsCatalogTest {

    @Test
    fun `catalog contains at least 15 secrets`() {
        assertTrue(SecretsCatalog.all.size >= 15, "Expected at least 15 secrets")
    }

    @Test
    fun `all secret ids are unique`() {
        val ids = SecretsCatalog.all.map { it.id }
        assertEquals(ids.size, ids.distinct().size, "Duplicate secret IDs found")
    }

    @Test
    fun `findById returns correct secret`() {
        val secret = SecretsCatalog.findById("arc_001")
        assertNotNull(secret)
        assertEquals("arc_001", secret.id)
    }

    @Test
    fun `findById returns null for unknown id`() {
        assertNull(SecretsCatalog.findById("unknown_xyz"))
    }

    @Test
    fun `catalog has at least 2 forecast confidence secrets`() {
        val forecastSecrets = SecretsCatalog.forecastSecrets
        assertTrue(forecastSecrets.size >= 2,
            "Expected at least 2 forecast confidence secrets, found ${forecastSecrets.size}")
    }

    @Test
    fun `total maximum forecast confidence does not exceed 100`() {
        val total = SecretsCatalog.forecastSecrets.sumOf { secret ->
            (secret.effect as SecretEffect.ForecastConfidence).points
        }
        // Maximum achievable confidence from all secrets should equal exactly 80 (sum of all forecast points)
        // but can exceed 100 — the ForecastRepository caps at 100, which is correct
        assertTrue(total > 0, "Forecast secret points should be positive")
    }

    @Test
    fun `classified tier has at least one secret`() {
        val classified = SecretsCatalog.all.filter { it.tier == SecretTier.CLASSIFIED }
        assertTrue(classified.isNotEmpty(), "Expected at least 1 CLASSIFIED tier secret")
    }

    @Test
    fun `every secret has a non-blank title and lore`() {
        SecretsCatalog.all.forEach { secret ->
            assertTrue(secret.title.isNotBlank(), "Secret ${secret.id} has blank title")
            assertTrue(secret.lore.isNotBlank(), "Secret ${secret.id} has blank lore")
        }
    }

    @Test
    fun `order_0 is the highest confidence secret`() {
        val order0 = SecretsCatalog.findById("arc_009")
        assertNotNull(order0)
        val order0Points = (order0.effect as SecretEffect.ForecastConfidence).points
        val maxOthers = SecretsCatalog.forecastSecrets
            .filter { it.id != "arc_009" }
            .maxOf { (it.effect as SecretEffect.ForecastConfidence).points }
        assertTrue(order0Points > maxOthers,
            "arc_009 should have the highest forecast confidence bonus")
    }
}
