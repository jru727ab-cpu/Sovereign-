# Catastrophe Cycle — CIVILTAS

> *"The first collapse was not predicted. The second one will be. Whether you survive it depends entirely on what you know before it arrives."*
> — Directorate Communiqué, Cycle 7, Opening Statement

---

## Overview

The **Catastrophe Cycle** is CIVILTAS's core retention and tension engine. Unlike a simple countdown timer, the catastrophe is **probabilistic** — players know something is coming, but not exactly when. This uncertainty is deliberate: it mirrors the game's narrative (even the Directorate doesn't know), creates genuine strategic tension, and gives the Secrets system meaningful gameplay purpose.

---

## The Forecast Meter

The Forecast Meter is a UI element displaying two values:

```
[████████░░░░░░░░░░░░]  Confidence: 42 / 100
Estimated arrival window:  ~18–60 days
```

### Confidence Score (0–100)

Confidence measures how well the player (and their society) understands the coming catastrophe. It is **not** a damage reduction stat — it is **information quality**.

Higher confidence means:
- The arrival window narrows (less uncertainty)
- Special preparation actions become available
- Certain Secrets can only be interpreted when confidence is high enough
- The player can make *informed* strategic decisions (hoard vs build now vs spread knowledge)

Low confidence means:
- Wide uncertainty window — the catastrophe could arrive next week or next year
- Forced reactive play — building for now because you don't know when "later" is
- Higher chance of being caught underprepared

### How Confidence Is Gained

| Source | Confidence Added |
|---|---|
| Unlock `arc_001` Zone 7 Survey | +5 |
| Unlock `arc_006` Collapse Chronology | +10 |
| Unlock `arc_007` The Initiating Event | +20 |
| Unlock `arc_008` Secondary Wave Model | +15 |
| Unlock `arc_009` Order 0: Who Started It | +30 |
| Complete a Forecast Expedition | +3–8 (variable) |
| Reach Society Rank III | +5 |

### Confidence Thresholds

| Confidence | Effect |
|---|---|
| 0–29 | Wide window (18–180 days). No preparation actions. |
| 30–59 | Narrowing window (18–90 days). Basic preparations available. |
| 60–79 | Focused window (18–45 days). Contingency actions unlock. |
| 80–99 | Tight window (18–28 days). Advanced preparations + special builds unlock. |
| 100 | Exact window known. All preparations available. End-game milestone. |

---

## Cycle Structure

Each catastrophe cycle follows this structure:

```
[Build Phase] → [Warning Phase] → [Impact Phase] → [Recovery Phase] → [New Build Phase]
```

### Build Phase (majority of a cycle)
- Normal gameplay: mine, refine, build, collect Secrets
- Forecast meter is visible but the window is wide
- Players pursue whichever philosophy suits them (Archivists / Hoarders / Builders)
- Secrets can be earned or purchased to increase confidence

### Warning Phase (triggered when cycle approaches)
- Forecast window narrows regardless of confidence (story progression)
- Special warning events fire (tremors, supply chain disruptions, strange readings)
- Players who ignored the forecast face harder choices under time pressure
- Players with high confidence get early warning indicators

### Impact Phase
- The catastrophe strikes — the exact form depends on the cycle type (seismic, resource scarcity, electromagnetic collapse, social fracture)
- Players with high confidence unlock special **Contingency responses**
- Archivists access hidden solutions; Hoarders draw from stockpiles; Builders use redundant infrastructure
- Unprepared players suffer heavier consequences but are never completely eliminated (the game continues)

### Recovery Phase
- Assess damage, rebuild critical systems
- "Post-collapse secrets" unlock — testimony, forensic analysis of the event
- Lore reveals about what just happened and hints at the *next* cycle
- Season Pass premium track delivers its climax rewards here

---

## Three Preparation Philosophies

Players are never forced into one strategy, but each has distinct risk/reward during cycles:

### Archivists (Pass on Knowledge)
- **Build**: Gnosis labs, libraries, society ranks, expedition teams
- **Benefit**: Unlock hidden contingency options during catastrophe; better post-collapse recovery bonuses; higher forecast confidence growth
- **Risk**: Slower raw output; may have less material stockpile at impact

### Hoarders (Stockpile Resources)
- **Build**: Expanded storage, extraction upgrades, supply chains
- **Benefit**: Best survival rate during resource scarcity catastrophes; fastest material rebuild
- **Risk**: May not understand *why* the catastrophe happened; no special Contingency options

### Builders (Thrive Now)
- **Build**: Infrastructure, population, output chains
- **Benefit**: Highest current production; fastest pre-catastrophe point accumulation
- **Risk**: Highest exposure if catastrophe strikes before defensive measures are in place

---

## Cycle Types (v1 — Planned)

| Cycle Name | Primary Threat | Benefit to Archivists | Benefit to Hoarders | Benefit to Builders |
|---|---|---|---|---|
| **The Tremor** | Infrastructure damage | Predicted safe zones avoid damage | Stored materials speed rebuild | Redundant buildings survive |
| **The Scarcity** | Resource depletion | Secret intel reveals hidden deposits | Stockpile bridges the gap | High production slows depletion |
| **The Signal** | Electronic/comms collapse | Geometric comm methods still work | Manual systems unaffected | Redundant relay grid survives |
| **The Fracture** | Social/population collapse | Testimony secrets provide morale | Morale sustained by full stores | Large population buffers losses |

---

## Season Pass Integration

Each catastrophe cycle maps to one **season**:

```
Build Phase → Warning Phase → Impact Phase → Recovery Phase
     = Season Track Free tier + Premium tier
```

- **Free track**: basic cycle rewards (resources, common secrets, building tokens)
- **Premium track (Season Pass)**: rare/classified secrets, cosmetic cycle-exclusive rewards, early intel on next cycle type, special Contingency action during Impact Phase

The Season Pass is the **cleanest long-term monetisation vehicle**: it requires no surprise purchases, the player knows what they're buying, and it feels like a TV season — self-contained with a satisfying arc.

---

## Design Principles

1. **Uncertainty is the game.** A perfect countdown timer removes tension. The Forecast Meter is deliberately noisy.
2. **Secrets are the answer to uncertainty.** Earning/buying secrets is the only way to narrow the window. This gives them genuine gameplay value, not just cosmetic appeal.
3. **Catastrophes are survivable, not punishing.** Even a completely unprepared player continues the game — they just face harder recovery. The catastrophe should feel dramatic, not unfair.
4. **No cycle is identical.** Cycle types rotate. Players cannot simply optimise once and coast — each cycle may demand a different preparation mix.
5. **The "why" is never fully resolved.** Lore reveals escalate the mystery. By Cycle 3–4, players understand enough to be unsettled. Full truth is reserved for long-term play and classified secrets.
