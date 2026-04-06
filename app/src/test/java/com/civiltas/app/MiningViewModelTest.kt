package com.civiltas.app

import com.civiltas.app.data.MiningPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [MiningViewModel] state logic.
 *
 * These tests use a [FakeMiningPreferences] stub so no Android context is needed,
 * and a [StandardTestDispatcher] to control coroutine time.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MiningViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** In-memory stub replacing SharedPreferences. */
    private class FakeMiningPreferences : MiningPreferences {
        override var totalOre: Long = 0L
        override var orePerTap: Long = MiningPreferences.DEFAULT_ORE_PER_TAP
        override var passiveOrePerHour: Long = MiningPreferences.DEFAULT_PASSIVE_RATE
        override var lastTimestampMillis: Long = System.currentTimeMillis()
        override var lifetimeTaps: Long = 0L
    }

    private fun buildViewModel(prefs: FakeMiningPreferences = FakeMiningPreferences()) =
        MiningViewModel(prefs)

    // ── Active tapping ────────────────────────────────────────────────────────

    @Test
    fun `onTapMine increments totalOre by orePerTap`() {
        val vm = buildViewModel()
        val initial = vm.uiState.value.totalOre

        vm.onTapMine()

        assertEquals(initial + MiningPreferences.DEFAULT_ORE_PER_TAP, vm.uiState.value.totalOre)
    }

    @Test
    fun `onTapMine increments sessionTaps`() {
        val vm = buildViewModel()

        vm.onTapMine()
        vm.onTapMine()
        vm.onTapMine()

        assertEquals(3, vm.uiState.value.sessionTaps)
    }

    @Test
    fun `onTapMine increments lifetimeTaps`() {
        val prefs = FakeMiningPreferences().apply { lifetimeTaps = 10L }
        val vm = buildViewModel(prefs)

        vm.onTapMine()

        assertEquals(11L, vm.uiState.value.lifetimeTaps)
    }

    @Test
    fun `onTapMine sets isMining to true`() {
        val vm = buildViewModel()

        vm.onTapMine()

        assertTrue(vm.uiState.value.isMining)
    }

    @Test
    fun `isMining resets to false after MINING_ANIM_DURATION_MS`() = runTest {
        val vm = buildViewModel()

        vm.onTapMine()
        advanceTimeBy(MiningViewModel.MINING_ANIM_DURATION_MS + 1)

        assertFalse(vm.uiState.value.isMining)
    }

    @Test
    fun `multiple taps accumulate ore correctly`() {
        val vm = buildViewModel()

        repeat(10) { vm.onTapMine() }

        assertEquals(10L * MiningPreferences.DEFAULT_ORE_PER_TAP, vm.uiState.value.totalOre)
    }

    // ── Passive collection ────────────────────────────────────────────────────

    @Test
    fun `onCollectPassive adds pendingPassiveOre to totalOre`() {
        // Simulate offline ore by having a 1-hour-old timestamp.
        val prefs = FakeMiningPreferences().apply {
            totalOre = 0L
            lastTimestampMillis = System.currentTimeMillis() - 3_600_000L // 1 hour ago
        }
        val vm = buildViewModel(prefs)

        val pendingBefore = vm.uiState.value.pendingPassiveOre
        assertTrue("Should have accrued offline ore", pendingBefore > 0)

        vm.onCollectPassive()

        assertEquals(pendingBefore, vm.uiState.value.totalOre)
        assertEquals(0L, vm.uiState.value.pendingPassiveOre)
    }

    @Test
    fun `onCollectPassive is a no-op when pendingPassiveOre is zero`() {
        val vm = buildViewModel()
        val totalBefore = vm.uiState.value.totalOre

        vm.onCollectPassive()

        assertEquals(totalBefore, vm.uiState.value.totalOre)
    }

    @Test
    fun `onCollectPassive resets passiveProgress to 0`() {
        val prefs = FakeMiningPreferences().apply {
            lastTimestampMillis = System.currentTimeMillis() - 3_600_000L
        }
        val vm = buildViewModel(prefs)
        vm.onCollectPassive()

        assertEquals(0f, vm.uiState.value.passiveProgress)
    }

    // ── Upgrade ───────────────────────────────────────────────────────────────

    @Test
    fun `onUpgradeTap increases orePerTap when affordable`() {
        val prefs = FakeMiningPreferences().apply { totalOre = 1_000L }
        val vm = buildViewModel(prefs)

        val prevPerTap = vm.uiState.value.orePerTap
        vm.onUpgradeTap()

        assertEquals(prevPerTap + 1L, vm.uiState.value.orePerTap)
    }

    @Test
    fun `onUpgradeTap deducts tapUpgradeCost from totalOre`() {
        val prefs = FakeMiningPreferences().apply { totalOre = 1_000L }
        val vm = buildViewModel(prefs)

        val costBefore = vm.uiState.value.tapUpgradeCost
        vm.onUpgradeTap()

        assertEquals(1_000L - costBefore, vm.uiState.value.totalOre)
    }

    @Test
    fun `onUpgradeTap is a no-op when totalOre is insufficient`() {
        val prefs = FakeMiningPreferences().apply { totalOre = 0L }
        val vm = buildViewModel(prefs)

        val perTapBefore = vm.uiState.value.orePerTap
        vm.onUpgradeTap()

        assertEquals(perTapBefore, vm.uiState.value.orePerTap)
    }

    // ── Offline accrual ───────────────────────────────────────────────────────

    @Test
    fun `offline ore is capped at PASSIVE_CAP_HOURS`() {
        val capMs = MiningPreferences.PASSIVE_CAP_HOURS * 3_600_000L
        val prefs = FakeMiningPreferences().apply {
            passiveOrePerHour = MiningPreferences.DEFAULT_PASSIVE_RATE
            // Simulate 24 hours away (exceeds 8-hour cap)
            lastTimestampMillis = System.currentTimeMillis() - 24 * 3_600_000L
        }
        val vm = buildViewModel(prefs)

        val maxExpected = capMs * prefs.passiveOrePerHour / 3_600_000L
        assertTrue(vm.uiState.value.pendingPassiveOre <= maxExpected)
    }

    @Test
    fun `zero offline time produces no offline ore`() {
        val prefs = FakeMiningPreferences().apply {
            lastTimestampMillis = System.currentTimeMillis()
        }
        val vm = buildViewModel(prefs)

        assertEquals(0L, vm.uiState.value.pendingPassiveOre)
    }

    // ── formatNumber helper ───────────────────────────────────────────────────

    @Test
    fun `formatNumber returns raw digits for small values`() {
        assertEquals("42", com.civiltas.app.ui.formatNumber(42L))
    }

    @Test
    fun `formatNumber uses K suffix for thousands`() {
        assertEquals("1.5K", com.civiltas.app.ui.formatNumber(1500L))
    }

    @Test
    fun `formatNumber uses M suffix for millions`() {
        assertEquals("2.0M", com.civiltas.app.ui.formatNumber(2_000_000L))
    }
}
