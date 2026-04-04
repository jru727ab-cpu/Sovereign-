# Gameplay Design — Sovereign

This document describes the core gameplay loops that drive moment-to-moment engagement in Sovereign.

---

## Table of Contents

1. [Loop Overview](#loop-overview)
2. [Resource Mining](#1-resource-mining)
3. [Building & Infrastructure](#2-building--infrastructure)
4. [Knowledge & Gnosis Discovery](#3-knowledge--gnosis-discovery)
5. [The Catastrophe Cycle](#4-the-catastrophe-cycle)
6. [Progression Tension: Build Now vs. Prepare](#5-progression-tension-build-now-vs-prepare)

---

## Loop Overview

Sovereign runs on three nested loops:

```
Micro loop  (seconds – minutes)  : Tap to mine → collect resources
Meso loop   (minutes – hours)    : Spend resources → build / research
Macro loop  (days – weeks)       : Navigate the Catastrophe Cycle → survive / prestige
```

Every loop feeds back into the others. Resources enable buildings; buildings accelerate resource gain and unlock knowledge; knowledge helps players survive the catastrophe cycle; surviving the cycle grants powerful bonuses that reset the micro/meso loops at a higher baseline.

---

## 1. Resource Mining

### Surface Mining (Tap)
Players tap on resource nodes on their territory map to manually extract materials. Each tap yields a small, immediate payout.

**Tier 1 — Surface Deposits** (available from day one)
| Resource | Use |
|---|---|
| Stone | Basic construction |
| Iron Ore | Tools, reinforcements |
| Copper | Wiring, components |
| Coal | Fuel for early buildings |
| Crude Oil | Advanced fuel, plastics |

**Tier 2 — Deep Mining** (unlocked via Foundry / Mine Shaft upgrades)
| Resource | Use |
|---|---|
| Gold | Currency, electronics |
| Silver | Electrical components, trade |
| Rare Earth Elements | High-tech research |
| Titanium | Late-game construction |
| Lithium | Energy storage systems |

### Idle / Passive Production
- Buildings like **Mine Shafts**, **Oil Derricks**, and **Refineries** produce resources continuously, even when the app is closed (see [progression.md](progression.md) for offline accumulation rules).
- Passive rate is always lower than active tapping to reward engagement.

### Resource Caps & Storage
- Each resource has a **storage cap** determined by the player's current Vault and Silo upgrades.
- Hitting the cap stops passive accumulation — incentivizing players to log in and collect or upgrade storage.
- The threat of a Catastrophe event adds urgency to strategic hoarding.

---

## 2. Building & Infrastructure

Buildings are the backbone of civilization progression. Every building serves one or more functions: production, research, defense, or narrative.

### Core Building Categories

| Category | Examples | Primary Function |
|---|---|---|
| **Production** | Mine Shaft, Oil Derrick, Foundry, Refinery | Increase passive resource output |
| **Knowledge** | Academy, Library, Observatory | Unlock tech tree nodes and Gnosis Fragments |
| **Defense** | Fortification Wall, Vault, Bunker | Reduce losses during Catastrophe events |
| **Population** | Housing District, Medical Center | Grow and sustain population (pop drives some production multipliers) |
| **Narrative** | Sanctuary, Secret Society HQ | Gate story content and special quests |

### Building Mechanics
- Buildings are constructed using resources and take real time to complete (or can be sped up via boosts).
- Each building has up to **5 upgrade levels**, with each level requiring more resources but providing exponentially better output.
- Buildings can be **destroyed** during a Catastrophe event if not adequately defended.

### City Layout (Future Feature)
In a future update, players will arrange buildings on a city grid, adding a spatial planning element. Adjacency bonuses will reward strategic placement (e.g., placing a Refinery next to an Oil Derrick).

---

## 3. Knowledge & Gnosis Discovery

This layer gives Sovereign its narrative depth and separates it from pure resource games.

### Tech Tree
The Tech Tree is a branching research graph covering:
- **Engineering** — construction speed, resource extraction efficiency
- **Science** — unlock new resource types, advanced buildings
- **Medicine** — population growth, disaster survival bonuses
- **History** — narrative unlocks, Gnosis Fragment hints
- **Esoterica** — Secret Society branch, Gnosis power unlocks

Each node requires both **resources** and **Academy/Library level** to unlock. Research takes real time.

### Gnosis Fragments
Gnosis Fragments are rare, lore-bearing items that tell the true story of the past catastrophe and hint at the next one.

**How Fragments are found:**
- Randomly generated during deep mining operations (low chance)
- Rewarded at the end of Secret Society quests
- Hidden in time-limited exploration events (e.g., "excavate an ancient ruin")
- Purchased with a special in-game currency (**Gnosis Shards**) in the Sanctum store

**What Fragments do:**
- Unlock narrative cutscenes / lore entries
- Grant permanent small stat bonuses (e.g., +2% gold yield)
- Progress the **Gnosis Meter**, which gates access to the Secret Society's inner circle and endgame content

### The Secret Society
A clandestine in-game faction with advance knowledge of the Catastrophe Cycle.

- Players interact with the Secret Society through a dedicated quest chain
- Society quests require rare resources and specific Gnosis Fragments to be completed
- Completing quests grants **Gnosis Shards** (premium narrative currency) and exclusive cosmetics
- The Society's lore gradually reveals the catastrophe timeline, giving players a strategic edge

---

## 4. The Catastrophe Cycle

The Catastrophe Cycle is Sovereign's macro-loop — the dramatic stakes that give all other gameplay meaning.

### Cycle Structure

```
Phase 1 — Rebuilding   : Standard play; mine, build, research (30+ days)
Phase 2 — Foreboding   : Secret Society hints appear; disaster metrics visible (7 days)
Phase 3 — The Event    : Catastrophe strikes; civilization is tested (real-time event, ~24 hrs)
Phase 4 — Aftermath    : Score calculated; rewards distributed; new cycle begins
```

### Event Types (planned)
| Event | Primary Threat | Defense Mechanic |
|---|---|---|
| Seismic Collapse | Buildings destroyed | Fortification level |
| Resource Plague | Stockpiles contaminated | Vault upgrades + rare materials |
| Solar Surge | Production halted | Observatory + Faraday upgrades |
| Invasion | Population loss | Military buildings (future feature) |

### Scoring & Rewards
Players are scored on:
- **Survival Rate**: what percentage of buildings survived
- **Resource Resilience**: how much stockpile remained
- **Gnosis Readiness**: whether the Secret Society warned them in time

Rewards scale with score and include:
- Bonus XP (up to 5 000 XP)
- Rare resource caches
- Exclusive cosmetics (only available during post-event Aftermath phase)
- Permanent civilization bonuses that carry into the next cycle

### Prestige (after max level)
See [progression.md](progression.md) for the full Prestige / Rebirth design. Surviving a Catastrophe at max level unlocks the option to **Prestige**, resetting the civilization but granting permanent multipliers and exclusive rewards.

---

## 5. Progression Tension: Build Now vs. Prepare

The central strategic tension of Sovereign is **resource allocation**:

> *Do I spend my gold on the next building upgrade (short-term gain), or hoard it in the Vault in case the Catastrophe hits? Do I research Science (immediate production boost) or Esoterica (Gnosis — long-term survival edge)?*

This tension is reinforced by:
- **Vault space** being limited (and upgrades costly)
- **Secret Society hints** being vague until higher Gnosis tiers
- **Buildings** being destructible in events
- **Resources in transit** (not yet in the Vault) being lost during events

Players who ignore preparation are not eliminated — they suffer a meaningful but recoverable setback, ensuring the game remains fun for all playstyles.
