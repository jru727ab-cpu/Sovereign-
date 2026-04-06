# CIVILTAS — Economy Design

## Core Resources

CIVILTAS has four primary resources, each with distinct roles:

| Resource | Icon | Role | Primary Source |
|----------|------|------|----------------|
| Ore | ⛏ | Universal currency; upgrade fuel | Automatic + tap |
| Stone | 🪨 | Construction; upgrade fuel | Automatic |
| Knowledge | 📚 | Gnosis progression; quest goals | Automatic |
| Energy | ⚡ | Activity cap; future stamina system | Regen |

---

## Resource Generation

All resources generate passively every second. Base rates are:

| Resource | Base Rate | Upgrade Bonus | Skill Bonus |
|----------|-----------|---------------|-------------|
| Ore | 0.10/s | +0.05/s per level | ×1.2–×1.4 |
| Stone | 0.05/s | +0.03/s per level | ×1.2–×1.35 |
| Knowledge | 0.01/s | +0.005/s per Gnosis level | ×1.3 |

**Effective rate formula:**
```
effectiveOreRate = (0.1 + oreUpgradeLevel × 0.05) × skillMultipliers
```

---

## Upgrade Economy

Upgrades consume Ore + Stone. The cost curve is designed so:

1. **Early levels** (1–5): Affordable within minutes of play
2. **Mid levels** (6–12): Require dedicated passive accumulation (hours)
3. **Late levels** (13–20): Long-term goals (days of play)

This prevents upgrades from feeling meaningless while ensuring there's always something to work toward.

### Upgrade Return on Investment

Each upgrade level increases production linearly while costs scale exponentially. This means:
- ROI is highest at low upgrade levels (buy them immediately)
- Late upgrades are prestige goals rather than pure efficiency plays

---

## Quest Economy

Quests create a secondary resource loop by providing lump-sum rewards:

- **Daily quests** reward ~200–300 XP and small resource bonuses
- **Rotating quests** reward 500–1000 XP, meaningful resources, and Skill Points
- **Check-in streak** rewards scale linearly, incentivizing consecutive play

Quest rewards are intentionally generous — they should feel like a bonus, not a crutch.

---

## Skill Point Economy

Skill Points are scarce:
- Source: +1 per level-up
- Source: Quest completion (+1–2 per rotating quest)
- No other sources planned for Phase 1

Players must make meaningful choices between trees. There are **14 SP worth of skills** in Phase 1; a typical player will have **~10 SP** available by level 10, forcing trade-offs.

---

## Catastrophe Economy

The Catastrophe cycle creates a soft time pressure without hard punishment:

- **Season duration:** 7 real-world days
- **At season end:** Progress and scores are evaluated; the catastrophe "event" resolves
- **Phase 1:** Season reset resets catastrophe progress only (no resource loss)
- **Phase 2:** Catastrophe will have meaningful consequences (resource tax, temporary debuffs)

This creates urgency without frustrating new players.

---

## Balance Principles

1. **Idle-first**: The game should feel rewarding when opened after hours away
2. **No dead time**: There should always be something to spend resources on
3. **No catch-22**: New players should never be unable to progress
4. **Exponential scaling, linear rewards**: Costs scale faster than rewards to extend the game's lifespan naturally
5. **Transparency**: All formulas are deterministic and player-readable
