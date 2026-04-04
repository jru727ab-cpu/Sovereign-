package com.civiltas.app.game

import kotlin.math.pow

object IdleEngine {
    fun calcOfflineGain(orePerSecond: Double, lastSaveMs: Long, nowMs: Long): Double {
        val elapsedSec = ((nowMs - lastSaveMs) / 1000.0).coerceIn(0.0, 8 * 3600.0)
        return orePerSecond * elapsedSec
    }

    fun upgradeOPS(current: Double, level: Int): Double = current * 1.15.pow(level.toDouble())

    fun upgradeCost(level: Int): Double = 50.0 * 1.6.pow(level.toDouble())

    fun manualCollectBonus(orePerSecond: Double): Double = orePerSecond * 1.0
}
