# Secrets System Design — Order of the Compass

## Overview

The **Secrets Library** is a collection of 12 data-driven secrets belonging to the fictional "Order of the Compass" — a secret society that predicted and documented the civilizational Catastrophe.

Secrets serve as the game's **narrative backbone** and the **primary driver** of the Catastrophe Forecast Meter.

---

## Data Model

Each `SecretEntry` has:

| Field | Type | Description |
|-------|------|-------------|
| `id` | String | Unique identifier |
| `title` | String | Display name (hidden when locked) |
| `category` | SecretCategory | Classification for filtering |
| `description` | String | Full lore text (revealed on unlock) |
| `hint` | String | Teaser shown while locked |
| `effect` | String | Gameplay or narrative benefit |
| `earnSource` | EarnSource | How to unlock |
| `isUnlocked` | Boolean | Runtime unlock state |

---

## Categories

| Category | Color | Description |
|----------|-------|-------------|
| LORE | Sky Blue | Historical and narrative secrets |
| SURVIVAL_INTEL | Red | Tactical and operational knowledge |
| RESOURCE_INTEL | Emerald | Mining and resource data |
| SACRED_GEOMETRY | Gold | Mathematical/architectural patterns |

---

## Earn Sources

| Source | Description |
|--------|-------------|
| MILESTONE | Automatically unlocked at ore milestones |
| EXPEDITION | Unlocked by completing expeditions (post-MVP) |
| RESEARCH | Unlocked through the research system (post-MVP) |
| PURCHASE | Unlocked via in-app purchase |

---

## The 12 Secrets

| # | ID | Title | Category | Earn Source |
|---|----|-------|----------|-------------|
| 1 | signal_01 | The First Signal | Lore | Milestone |
| 2 | hex_lattice | Hex Lattice Foundations | Sacred Geometry | Research |
| 3 | corridor_map | Safe Corridor Map Fragment | Survival Intel | Expedition |
| 4 | strata_reading | Resource Strata Reading | Resource Intel | Milestone |
| 5 | compass_cipher | The Compass Rose Cipher | Lore | Purchase |
| 6 | ley_line | Ley Line Survey | Sacred Geometry | Research |
| 7 | signal_02 | The Second Signal | Lore | Milestone |
| 8 | deep_vein | Deep Vein Sonar | Resource Intel | Expedition |
| 9 | convergence_protocol | Convergence Protocol | Survival Intel | Purchase |
| 10 | sacred_proportion | Sacred Proportion Blueprint | Sacred Geometry | Research |
| 11 | archivist_entry | The Archivist's Last Entry | Lore | Milestone |
| 12 | gnosis_01 | Gnosis Fragment I | Lore | Purchase |

---

## Catastrophe Forecast Meter

The meter is calculated as:

```
forecastPercent = (unlockedSecrets / totalSecrets) * 100
```

| Range | Status | Color |
|-------|--------|-------|
| 0% | UNKNOWN | Red |
| 1–29% | HIGH UNCERTAINTY | Red |
| 30–59% | MODERATE CLARITY | Amber |
| 60–100% | GOOD FORECAST | Emerald |

Locking secrets behind Milestones, Expeditions, Research, and Purchases creates **natural progression pressure** toward the forecast meter — incentivizing both free play and premium purchases.

---

## Future Expansion

- Secrets Tier 2 (post-MVP): 12 additional secrets unlocked in Season 1
- Secret combinations: Unlocking two related secrets triggers a "revelation" event
- VIP-exclusive secrets: Available only to Compass Bearer subscribers
