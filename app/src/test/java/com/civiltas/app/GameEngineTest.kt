package com.civiltas.app

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GameEngine — pure Kotlin, no Android/Robolectric required.
 */
class GameEngineTest {

    private lateinit var storage: InMemoryStorage
    private lateinit var engine: GameEngine

    @Before
    fun setUp() {
        storage = InMemoryStorage()
        engine = GameEngine(storage)
    }

    @Test
    fun `tick accumulates iron at correct rate`() {
        engine.loadAndApplyOfflineProgress(nowEpochSec = 1_000L)
        engine.tick(10L)
        // iron rate = 0.5/s, level 1 multiplier = 1.0
        assertEquals(5f, engine.state.iron, 0.01f)
    }

    @Test
    fun `tick accumulates gold at correct rate`() {
        engine.loadAndApplyOfflineProgress(nowEpochSec = 1_000L)
        engine.tick(10L)
        assertEquals(1f, engine.state.gold, 0.01f)
    }

    @Test
    fun `tick awards xp correctly`() {
        engine.loadAndApplyOfflineProgress(nowEpochSec = 1_000L)
        engine.tick(10L) // 10s → 1 XP
        assertEquals(1, engine.state.xp)
    }

    @Test
    fun `offline progress capped at 8 hours`() {
        // Save with lastSaveEpoch = 0
        storage.saveLong("last_save", 0L)
        // Load with nowEpochSec = 24 hours later
        engine.loadAndApplyOfflineProgress(nowEpochSec = 24L * 3600)
        // Iron: 0.5/s * 8h * 3600s = 14400
        val expected = Resource.IRON.ratePerSecond * 8 * 3600
        assertEquals(expected, engine.state.iron, 1f)
    }

    @Test
    fun `level up occurs when xp threshold crossed`() {
        engine.loadAndApplyOfflineProgress(nowEpochSec = 1_000L)
        // Need 100 XP (level 1 threshold): 100 XP = 1000 seconds
        engine.tick(1_000L)
        assertEquals(2, engine.state.level)
        // Excess XP = 100 - (1 * 100) = 0 (hit threshold exactly)
        assertEquals(0, engine.state.xp)
    }

    @Test
    fun `save and reload preserves state`() {
        engine.loadAndApplyOfflineProgress(nowEpochSec = 1_000L)
        engine.tick(60L)
        val ironBefore = engine.state.iron
        val xpBefore = engine.state.xp

        engine.save(nowEpochSec = 1_060L)

        val engine2 = GameEngine(storage)
        engine2.loadAndApplyOfflineProgress(nowEpochSec = 1_060L) // no elapsed time
        assertEquals(ironBefore, engine2.state.iron, 0.01f)
        assertEquals(xpBefore, engine2.state.xp)
    }

    @Test
    fun `catastrophe countdown decrements with real time`() {
        // Arrange: set last_save to T=0, catastrophe at full cycle
        storage.saveLong("last_save", 0L)
        storage.saveLong("catastrophe_cd", GameState.CATASTROPHE_CYCLE_SEC)
        // Load 1 hour later
        engine.loadAndApplyOfflineProgress(nowEpochSec = 3600L)
        val expected = GameState.CATASTROPHE_CYCLE_SEC - 3600L
        assertEquals(expected, engine.state.catastropheCountdownSec)
    }
}

// ── Test double ───────────────────────────────────────────────────────────────

class InMemoryStorage : StorageBackend {
    private val longs = mutableMapOf<String, Long>()
    private val floats = mutableMapOf<String, Float>()
    private val ints = mutableMapOf<String, Int>()

    override fun loadLong(key: String, default: Long) = longs.getOrDefault(key, default)
    override fun saveLong(key: String, value: Long) { longs[key] = value }

    override fun loadFloat(key: String, default: Float) = floats.getOrDefault(key, default)
    override fun saveFloat(key: String, value: Float) { floats[key] = value }

    override fun loadInt(key: String, default: Int) = ints.getOrDefault(key, default)
    override fun saveInt(key: String, value: Int) { ints[key] = value }
}
