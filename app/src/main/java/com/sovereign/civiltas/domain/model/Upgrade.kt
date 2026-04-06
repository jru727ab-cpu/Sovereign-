package com.sovereign.civiltas.domain.model

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val level: Int,
    val maxLevel: Int,
    val oreCost: Double,
    val stoneCost: Double,
    val effect: String // e.g. "+0.05 ore/s per level"
)

fun oreUpgrades(currentLevel: Int): Upgrade = Upgrade(
    id = "ore_extractor",
    name = "Ore Extractor",
    description = "Improves ore extraction rate by 50% per level.",
    level = currentLevel,
    maxLevel = 20,
    oreCost = 50.0 * Math.pow(1.6, currentLevel.toDouble()),
    stoneCost = 20.0 * Math.pow(1.4, currentLevel.toDouble()),
    effect = "+${String.format("%.2f", 0.05 * (currentLevel + 1))} ore/s"
)

fun stoneUpgrades(currentLevel: Int): Upgrade = Upgrade(
    id = "stone_drill",
    name = "Stone Drill",
    description = "Carves stone faster per level.",
    level = currentLevel,
    maxLevel = 20,
    oreCost = 30.0 * Math.pow(1.5, currentLevel.toDouble()),
    stoneCost = 10.0 * Math.pow(1.6, currentLevel.toDouble()),
    effect = "+${String.format("%.2f", 0.03 * (currentLevel + 1))} stone/s"
)

fun energyUpgrades(currentLevel: Int): Upgrade = Upgrade(
    id = "power_cell",
    name = "Power Cell",
    description = "Increases energy capacity and regen.",
    level = currentLevel,
    maxLevel = 10,
    oreCost = 80.0 * Math.pow(2.0, currentLevel.toDouble()),
    stoneCost = 40.0 * Math.pow(1.8, currentLevel.toDouble()),
    effect = "+${50 * (currentLevel + 1)} energy cap"
)
