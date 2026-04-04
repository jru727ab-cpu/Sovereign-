# CIVILTAS

**CIVILTAS** is a hybrid idle/strategy survival game set in the aftermath of a catastrophic collapse. Players rebuild civilization while uncovering the hidden truths held by the **Order of the Compass** — a secret society that preserved forbidden knowledge before the fall.

## Core Concept

> *"The compass does not point north. It points toward what must be preserved."*

The world has suffered a catastrophic event. Another is coming — and only the Order of the Compass knows the signs.

Players must choose their path:

| Path | Focus | Benefit |
|------|-------|---------|
| **Archivist** | Knowledge, gnosis, Order ranks | Unlock hidden solutions during catastrophe |
| **Hoarder** | Resources, storage, supply chains | Survive scarcity, rebuild faster |
| **Builder** | Expansion, infrastructure, population | High current output (higher risk if unprepared) |

## Gameplay Pillars

1. **Idle Resource Loop** — Mine → Collect → Refine → Build → Unlock → Repeat
2. **Gnosis / Secrets Track** — Collect Order teachings to unlock new mechanics and buildings
3. **Catastrophe Forecast** — A meter of uncertainty; the right Secrets increase your confidence
4. **Daily/Weekly Objectives** — Rotating tasks and streaks to keep the world alive

## The Order of the Compass

The Order of the Compass is a clandestine society that survived the first catastrophe by preserving knowledge across three pillars:

- **Sacred Geometry** — blueprints, layouts, and structural formulas lost to history
- **Survival Intel** — maps of safe corridors, resource strata, and early-warning signs
- **Lore Dossiers** — accounts of what really happened, who caused it, and what is coming

Players earn **Compass Rank** by collecting Secrets. Higher ranks unlock advanced teachings and exclusive Order ceremonies.

### Compass Ranks

| Rank | Name | Requirement |
|------|------|-------------|
| 0 | Seeker | Join the Order |
| 1 | Initiate | Collect 5 Secrets |
| 2 | Candidate | Collect 15 Secrets |
| 3 | Adept | Collect 30 Secrets |
| 4 | Keeper | Collect 50 Secrets |
| 5 | Guardian | Collect 75 Secrets + complete Catastrophe Forecast |
| 6 | Archon | Collect all Secrets |

## Monetization

Monetization is designed to be fair — free players can always progress:

- **Secrets Packs** — lore bundles, sacred geometry sets, survival intel dossiers
- **Season Pass** — each catastrophe cycle is a season (free + premium tracks)
- **VIP Subscription** — convenience + cosmetics + extended offline cap
- **Optional Rewarded Ads** — player-chosen boosts (no forced ads)
- **One-time "Remove Ads"** — replaces ad prompts with small daily bonuses

## Project Structure

```
CIVILTAS/
├── README.md
├── index.html              # Interactive prototype / web preview
├── docs/
│   ├── secrets.md          # Order of the Compass teachings and dossiers
│   └── monetization.md
└── src/
    └── secrets/
        └── SecretsConstants.kt   # Android MVP UI string constants
```

## Development

This MVP is built as a web prototype + Android Kotlin stubs.

- **Web prototype**: open `index.html` in a browser
- **Android**: see `src/secrets/SecretsConstants.kt` for UI string constants

## Theme

The **CIVILTAS** name reflects a hybrid tone: grounded post-collapse engineering layered over esoteric knowledge and sacred geometry. The **Order of the Compass** bridges both — analytical in method, mystical in origin.
