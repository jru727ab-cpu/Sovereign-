# CIVILTAS — Progression Design

## Overview

Progression in CIVILTAS is multi-layered, giving players short, medium, and long-term goals simultaneously. The core loop is: **mine → upgrade → unlock skills → survive catastrophe → ascend**.

---

## Player Level

Players gain **XP** passively (1 XP/second) and through quests, daily check-ins, and offline progress. Level thresholds follow a quadratic curve:

```
XP required for level N = 100 × N²
```

| Level | XP Required | Cumulative XP |
|-------|------------|---------------|
| 1     | 100        | 100           |
| 2     | 400        | 500           |
| 5     | 2,500      | ~6,500        |
| 10    | 10,000     | ~38,500       |
| 20    | 40,000     | ~280,000      |

**On level-up:** Player receives +1 Skill Point.

---

## Skill Points & Skill Trees

Skill Points are the primary upgrade currency for long-term power. Three trees exist:

### 🪨 Miner Tree
Focused on ore production and offline efficiency.

| Skill | Cost | Effect | Requires |
|-------|------|--------|----------|
| Deep Vein Tap | 1 SP | Ore rate ×1.2 | — |
| Iron Lungs | 2 SP | Offline cap +1h | Deep Vein Tap |
| Vein Sight | 3 SP | Ore rate ×1.4 | Iron Lungs |

### 🏗 Builder Tree
Focused on stone and construction speed.

| Skill | Cost | Effect | Requires |
|-------|------|--------|----------|
| Rapid Quarry | 1 SP | Stone rate ×1.2 | — |
| Efficient Cuts | 2 SP | Stone rate ×1.35 | Rapid Quarry |

### 📚 Scholar Tree
Focused on knowledge and XP multipliers.

| Skill | Cost | Effect | Requires |
|-------|------|--------|----------|
| Ancient Scripts | 1 SP | Knowledge rate ×1.3 | — |
| Gnosis Boost | 2 SP | XP gain ×1.25 | Ancient Scripts |

---

## Gnosis Rank

**Gnosis** is a prestige-lite system that unlocks new mechanics without a full reset. Each rank requires accumulating `rank × 100` knowledge units. Gnosis level increases the base knowledge rate and will unlock new quest chains and mechanics in future phases.

---

## Upgrade Levels

Each extractor (Ore, Stone, Power Cell) has **20 levels** with exponentially scaling costs and linear production gains. This creates satisfying early progression that slows into a longer-term goal.

**Cost formula:**
- Ore Extractor: `50 × 1.6^level` ore + `20 × 1.4^level` stone
- Stone Drill: `30 × 1.5^level` ore + `10 × 1.6^level` stone  
- Power Cell: `80 × 2.0^level` ore + `40 × 1.8^level` stone

---

## Offline Progress

The offline engine is a core loop accelerator. Players return to see meaningful progress, capped by their current offline cap (default: 1 hour). The Power Cell upgrade and Miner skills extend this cap significantly.

**Offline gains formula:**
```
elapsed = min(realElapsed, offlineCap)
oreGained = orePerSecond × elapsed
```

The offline gains dialog on re-entry creates a satisfying "return moment" that drives daily engagement.

---

## Quest System

Quests provide structured short-term goals:

- **Daily quests** reset each day and include a check-in streak
- **Rotating quests** (3 active at a time) are hand-crafted mid-term goals
- **Story quests** (Phase 2) will drive the narrative arc

Rewards scale with quest difficulty and include XP, resources, and Skill Points.

---

## Daily Check-In Streak

Each consecutive day of check-in multiplies the reward:

```
bonusOre = 50 × streak
bonusXp = 100 × streak
```

Streaks reset if a full calendar day is missed. This drives habitual daily engagement without being punishing.
