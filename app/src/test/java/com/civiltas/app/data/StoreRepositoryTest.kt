package com.civiltas.app.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for [StoreRepository] — verifies catalogue integrity and SKU coverage.
 */
class StoreRepositoryTest {

    @Test
    fun `catalogue is not empty`() {
        assertTrue(StoreRepository.catalogue.isNotEmpty())
    }

    @Test
    fun `all SKU ids are unique`() {
        val ids = StoreRepository.catalogue.map { it.skuId }
        assertEquals(ids.size, ids.distinct().size, "Duplicate SKU IDs found")
    }

    @Test
    fun `season pass is present in catalogue`() {
        val seasonPass = StoreRepository.findBySku("season_pass")
        assertNotNull(seasonPass, "Expected season_pass SKU in catalogue")
    }

    @Test
    fun `all secret ids in catalogue exist in SecretsCatalog`() {
        val allCatalogIds = SecretsCatalog.all.map { it.id }.toSet()
        StoreRepository.catalogue.forEach { item ->
            item.secretIds.forEach { secretId ->
                assertTrue(
                    secretId in allCatalogIds,
                    "Store SKU ${item.skuId} references unknown secret id: $secretId"
                )
            }
        }
    }

    @Test
    fun `all items have non-blank display names and descriptions`() {
        StoreRepository.catalogue.forEach { item ->
            assertTrue(item.displayName.isNotBlank(), "SKU ${item.skuId} has blank displayName")
            assertTrue(item.description.isNotBlank(), "SKU ${item.skuId} has blank description")
        }
    }

    @Test
    fun `purchase stub returns error not success`() {
        var gotSuccess = false
        var gotError = false
        StoreRepository.initiatePurchase(
            skuId = "secrets_pack_archivist_common",
            onSuccess = { gotSuccess = true },
            onError = { gotError = true }
        )
        assertTrue(gotError, "Expected stub to invoke onError")
        assertTrue(!gotSuccess, "Stub should not invoke onSuccess")
    }
}
