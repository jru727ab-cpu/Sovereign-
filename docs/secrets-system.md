# CIVILTAS Secrets System — Design Document

## 1. Overview

**Secrets** are collectible knowledge entries that drip-feed lore, strategic intelligence, and mechanic unlocks to the player. They form the backbone of the *Gnosis / Secret Society* progression track and answer CIVILTAS's central tension:

> *Another catastrophe is coming — but you don't know when. Do you acquire knowledge to pass on, hoard resources for the future, or build for now and survive?*

Secrets feel valuable because they are: they change what the player *can* do, *know*, and *prepare for*.

---

## 2. Secret Categories

| Category | What It Reveals | Primary Effect |
|---|---|---|
| **Lore** | Who caused the catastrophe, what was lost, hidden history | Narrative satisfaction; unlocks Secret Society dialogue; no hard mechanical advantage |
| **Strategy Intel** | Safest migration corridors, hazard zone maps, stockpile guidance | Reduces resource loss during catastrophe events; improves evacuation efficiency |
| **Knowledge / Blueprint** | Sacred geometry, forgotten construction methods, advanced crafting | Unlocks new building types, new mining site access, or research branch gates |
| **Forecast** | Anomaly patterns, precursor signs of the next event | Improves *Catastrophe Forecast* confidence (see §6) |

A single Secret can span multiple categories (e.g., a lore entry that also provides a small forecast bonus).

---

## 3. What a Secret Is (Data Model)

```
Secret {
  id:           String          // unique identifier, e.g. "lore_first_fracture"
  title:        String          // display name
  category:     SecretCategory  // LORE | INTEL | BLUEPRINT | FORECAST
  teaser:       String          // spoiler-free preview shown while locked
  fullText:     String          // full content shown after unlock
  effects:      Effect[]        // mechanical changes applied on unlock
  unlockConditions: Condition[] // how it can be earned for free
  skuId:        String | null   // non-null if also purchasable
  isLocked:     Boolean         // derived from player state
}

Effect {
  type:    EffectType  // MINING_YIELD | BUILDING_UNLOCK | FORECAST_BONUS | RISK_REDUCTION
  target:  String      // what building/resource/system is affected
  value:   Number      // magnitude (percentage or flat)
}

Condition {
  type:    ConditionType  // GNOSIS_LEVEL | MILESTONE | QUEST_COMPLETE | EXPEDITION_RETURN
  target:  String
  value:   Number | String
}
```

---

## 4. Discovery vs Purchase

Every Secret has **at least one earnable path** — no core-progression content is hard-paywalled.

### Earnable Paths (Free)
| Method | Description |
|---|---|
| **Gnosis Level** | Reach a Gnosis rank to automatically unlock a lore entry |
| **Expedition returns** | Send a scouting party; they return with intel |
| **Milestone completion** | Build a Library, reach a population threshold, survive a catastrophe |
| **Daily/Weekly objectives** | Rotating tasks that reward a small number of Secrets or Fragments |
| **Secret Fragments** | Collect fragments via gameplay; combine to unlock a full Secret |

### Purchasable Paths (IAP)
- **Instant unlock**: skip the earnable grind for a specific Secret ($0.99–$2.99).
- **Chapter bundles**: a thematic bundle of 3–5 related Secrets at a discount.
- **Season Pass**: premium track releases Secrets ahead of the free schedule over a catastrophe cycle.

### Anti-predatory Guardrails
1. Purchased Secrets **never exceed** the power ceiling of their earnable counterparts.
2. The *core gameplay loop* (mine → build → survive) **does not require** any paid Secret.
3. Every purchased Secret can eventually be earned free — purchase only accelerates timing.
4. Secrets that affect *narrative closure* (ending lore) are **always earnable** in full.

---

## 5. Gnosis / Secret Society Integration

The **Gnosis Track** is a parallel XP system separate from the main Civilization level.

```
Gnosis XP Sources:
  - Studying at the Library (idle, slow)
  - Completing Knowledge Expeditions
  - Unlocking a Secret (discovery bonus)
  - Solving in-world puzzles / sigils

Gnosis Ranks:
  Rank 0 — Seeker        (starts here)
  Rank 1 — Initiate      (unlocks: Lore Tier 1, basic forecast access)
  Rank 2 — Adept         (unlocks: Intel Tier 1, expeditions)
  Rank 3 — Archivist     (unlocks: Blueprint Tier 1, fragment crafting)
  Rank 4 — Elder Scholar (unlocks: Forecast Tier 2, Society council dialogue)
  Rank 5 — Keeper        (unlocks: all remaining earnable Secrets, endgame lore)
```

Each rank gate is transparent: the player always knows what Gnosis XP they need and how to get it.

---

## 6. Catastrophe Forecast Mechanic

The **Forecast Panel** shows the player's current prediction quality for the next catastrophe:

```
Forecast Confidence Levels:
  BLIND      — "You sense nothing. The next event could be years away or tomorrow."
  VAGUE      — "Anomalies are increasing. Prepare within 2 cycles."
  APPROXIMATE — "Signs point to an event within the next cycle."
  PRECISE    — "Imminent. You have roughly 7 days of in-game time to prepare."
  PINPOINT   — "You know the location, scale, and estimated time to the hour."
```

Each **Forecast-category Secret** unlocks additional sensor precision, narrowing the window. This creates genuine strategic value for Secrets beyond pure narrative — *and* makes them marketable without being mandatory.

---

## 7. Gameplay Effects Summary

| Secret | Effect | Notes |
|---|---|---|
| Lore: *The First Fracture* | +5% Gnosis XP rate; unlocks Secret Society dialogue branch | Atmospheric; minimal power |
| Intel: *The Safe Corridors* | –20% resource loss on evacuation events | Meaningful preparation advantage |
| Blueprint: *Foundation Stones (Sacred Geometry)* | Unlocks **Resonance Chamber** building | New production chain, not gating the base loop |
| Forecast: *Precursor Patterns* | Upgrades forecast from BLIND → VAGUE | Earliest forecast improvement |

Full effect table maintained in `js/secrets/catalog.js`.

---

## 8. Avoiding Spoilers While Marketing

- **Locked Secrets display a teaser** — evocative text that conveys the mystery without giving away the reveal. E.g.: *"Someone set this in motion deliberately. Their name is closer than you think."*
- **Category silhouette icons** distinguish Lore / Intel / Blueprint / Forecast entries without exposing content.
- **Marketing copy uses the teaser, never the full text** — preserves the reward of discovery.
- **Season Pass sells "earlier access"** — the same content players would get anyway, just sooner — which avoids the feel of "you can't know the story unless you pay."

---

## 9. Prep Philosophy Integration

Secrets should reinforce (not dictate) the player's chosen strategy:

| Philosophy | Best Secrets | Trade-off |
|---|---|---|
| **Archivist** (pass on knowledge) | Lore + Forecast | High Gnosis; low resources at event time |
| **Hoarder** (stockpile for future) | Intel + some Blueprint | Good survival rate; misses some story depth |
| **Builder** (thrive now) | Blueprint + Intel | Strong current output; vulnerable if forecast is poor |

No philosophy requires paid Secrets. Paid options help players *go deeper* in their chosen path, not change paths.

---

## 10. Future Expansion Hooks

- **Secret rarity tiers** (Common / Rare / Ancient / Legendary) for seasonal content.
- **Tradeable Secret Fragments** between players (if multiplayer is added).
- **Secret Society Council** — NPC characters whose questlines gate epic-tier Secrets.
- **Procedural intel Secrets** generated from the player's unique game history.
