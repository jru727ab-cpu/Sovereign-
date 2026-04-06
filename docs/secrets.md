# Secrets System — CIVILTAS

> *"A secret held by one is power. A secret shared with many is a civilisation."*
> — Order of the Eternal Compass, Inscription IV

---

## Overview

Secrets are collectible **knowledge artefacts** that sit at the heart of the CIVILTAS progression loop. Each Secret is a data card combining:

- **Narrative**: world-building, lore reveals, society history
- **Strategic value**: information that improves player decision-making
- **Mechanical effect**: small but meaningful boosts or unlocks

Secrets are not pay-to-win upgrades. They are **lore you can act on**.

---

## The Two Pillars of the Secret Society

### The Directorate of Archivists
A cold, methodical intelligence network that catalogued the collapse with clinical precision. Their Secrets are filed under operational codenames: asset assessments, zone surveys, probability matrices, evacuation protocols.

- **Tone**: grounded, analytical, redacted government documents
- **Effect type**: forecast confidence, resource maps, efficiency bonuses
- **Colour**: deep blue / slate

### The Order of the Eternal Compass
An ancient esoteric tradition that pre-dates the collapse by centuries. The Order studies sacred geometry encoded into surviving architecture, natural formations, and pre-collapse blueprints.

- **Tone**: mystical, symbolic, encoded in glyphs and diagrams
- **Effect type**: blueprint unlocks, hidden building layouts, morale bonuses
- **Colour**: gold / amber

Both pillars are **factions within the same society**. Players do not choose one — they collect Secrets from both, and the tension between cold data and sacred geometry creates the game's narrative spine.

---

## Secret Anatomy

```kotlin
data class Secret(
    val id: String,
    val title: String,
    val category: SecretCategory,
    val tier: SecretTier,
    val lore: String,          // 2–4 sentence flavour text
    val effectDescription: String,
    val effect: SecretEffect,
    val unlockSource: UnlockSource,
    val storeSkuId: String?    // null = earn-only; non-null = also purchasable
)
```

### Categories

| Category | Pillar | Description |
|---|---|---|
| `DIRECTORATE_INTEL` | Archivists | Cold-case intelligence files, probability assessments |
| `CONTINUITY_PROTOCOL` | Archivists | Pre-collapse evacuation and resilience protocols |
| `ORDER_GEOMETRY` | Order | Sacred geometry diagrams and encoded building layouts |
| `SURVIVOR_TESTIMONY` | Mixed | Fragmented accounts; bridge lore between pillars |

### Tiers

| Tier | Unlock Difficulty | Effect Magnitude | Purchasable? |
|---|---|---|---|
| `COMMON` | Early game / low milestone | Flavour + tiny boost | Optional pack |
| `UNCOMMON` | Mid-game milestone | Small mechanic unlock | Optional pack |
| `RARE` | Expedition or high milestone | Meaningful unlock | Premium pack |
| `CLASSIFIED` | Society rank or Season Pass | Significant story + effect | Season Pass / Premium |

### Effect Types

| Effect | Description |
|---|---|
| `FORECAST_CONFIDENCE` | Adds `+N` points to the Forecast confidence meter |
| `RESOURCE_EFFICIENCY` | Reduces resource cost or increases yield by a percentage |
| `BLUEPRINT_UNLOCK` | Unlocks a building type that cannot otherwise be built |
| `EXPEDITION_BONUS` | Improves expedition success rate or loot quality |
| `MORALE_BOOST` | Increases population morale, improving task completion speed |
| `OFFLINE_BONUS` | Improves resources earned while app is closed |
| `LORE_ONLY` | No mechanical effect; pure story reveal |

---

## Full Secrets Catalog (v1 — 15 entries)

### Tier: COMMON

| ID | Title | Category | Effect |
|---|---|---|---|
| `arc_001` | Zone 7 Contamination Survey | DIRECTORATE_INTEL | FORECAST_CONFIDENCE +5 |
| `arc_002` | Pre-Collapse Population Density Map | DIRECTORATE_INTEL | EXPEDITION_BONUS +3% |
| `arc_003` | The Pulse Anomaly (Fragmented Log) | SURVIVOR_TESTIMONY | LORE_ONLY |
| `ord_001` | The Fibonacci Stockpile Arrangement | ORDER_GEOMETRY | RESOURCE_EFFICIENCY +2% |
| `ord_002` | First Glyph: The Anchor | ORDER_GEOMETRY | LORE_ONLY |

### Tier: UNCOMMON

| ID | Title | Category | Effect |
|---|---|---|---|
| `arc_004` | Evacuation Protocol Delta-7 | CONTINUITY_PROTOCOL | OFFLINE_BONUS +5% |
| `arc_005` | Asset Recovery Codex, Vol. I | DIRECTORATE_INTEL | RESOURCE_EFFICIENCY +5% |
| `arc_006` | Collapse Chronology: First 72 Hours | DIRECTORATE_INTEL | FORECAST_CONFIDENCE +10 |
| `ord_003` | Sacred Geometry: The Resilient Arch | ORDER_GEOMETRY | BLUEPRINT_UNLOCK (Reinforced Shelter) |
| `sur_001` | Testimony of Archivist Vael | SURVIVOR_TESTIMONY | MORALE_BOOST +5% |

### Tier: RARE

| ID | Title | Category | Effect |
|---|---|---|---|
| `arc_007` | Classified: The Initiating Event | DIRECTORATE_INTEL | FORECAST_CONFIDENCE +20 |
| `arc_008` | The Secondary Wave Model | CONTINUITY_PROTOCOL | FORECAST_CONFIDENCE +15 |
| `ord_004` | The Harmonic Lattice Blueprint | ORDER_GEOMETRY | BLUEPRINT_UNLOCK (Resonance Forge) |
| `sur_002` | The Last Broadcast (Reconstructed) | SURVIVOR_TESTIMONY | MORALE_BOOST +10%, LORE_ONLY |

### Tier: CLASSIFIED

| ID | Title | Category | Effect |
|---|---|---|---|
| `arc_009` | Order 0: Who Started It | DIRECTORATE_INTEL | FORECAST_CONFIDENCE +30, LORE_ONLY |

---

## Unlock Sources

### Earnable (Free)

| Source | How | Example Secret |
|---|---|---|
| **Daily Task Milestone** | Complete 7-day streak | `arc_001` (Common) |
| **Expedition Milestone** | Reach Expedition tier 3 | `arc_004` (Uncommon) |
| **Gnosis Research** | Unlock Society Rank II | `ord_003` (Uncommon) |
| **Deep Expedition** | Complete special region | `arc_007` (Rare) |

### Purchasable (Store)

| SKU | Contents | Tier |
|---|---|---|
| `secrets_pack_archivist_common` | 3 Common Directorate secrets | Common |
| `secrets_pack_order_common` | 3 Common Order secrets | Common |
| `secrets_pack_intel_vol1` | 2 Uncommon + 1 Rare Directorate | Uncommon–Rare |
| `secrets_pack_geometry_vol1` | 2 Uncommon + 1 Rare Order | Uncommon–Rare |
| `secrets_pack_classified_bundle` | The 1 Classified secret | Classified |
| `season_pass` | Full premium track for current cycle | All tiers |

---

## Forecast Confidence Integration

The **Forecast Meter** runs from 0–100 confidence. Certain Secrets contribute directly:

| Secret | Confidence Added |
|---|---|
| `arc_001` Zone 7 Survey | +5 |
| `arc_006` Collapse Chronology | +10 |
| `arc_007` The Initiating Event | +20 |
| `arc_008` Secondary Wave Model | +15 |
| `arc_009` Order 0 | +30 |

At **60+ confidence**: the forecast window narrows (fewer false alarms, better prep time).  
At **80+ confidence**: players unlock special "Contingency" preparation actions.  
At **100 confidence**: the exact catastrophe arrival window is known (rare/end-game achievement).

See [`docs/catastrophe-cycle.md`](catastrophe-cycle.md) for full mechanics.

---

## Design Guardrails

1. **No secret gates survival entirely behind a paywall.** Every confidence-boosting secret has at least one earnable path.
2. **Purchased secrets accelerate access** — they do not provide exclusive permanent stat advantages unavailable by playing.
3. **Lore secrets are a safe monetisation zone** — players buy story, not unfair power.
4. **CLASSIFIED tier** is the one high-investment purchase/unlock; it is a story climax, not a mechanical hard wall.
