# CIVILTAS — Full Game Design Document

**Version:** 0.1 (MVP Scope)  
**Platform:** Android (minSdk 26 / Android 8.0+)  
**Genre:** Idle / Incremental / Narrative  
**Studio:** Sovereign Interactive

---

## 1. Vision Statement

> A catastrophe destroyed the old world. You survived. Now you mine the earth to rebuild — but a secret society already knows a second catastrophe is coming. Do you build for now, or hoard for the storm?

SOVEREIGN combines idle resource extraction with a layered narrative mystery. Players are hooked by the idle loop and kept engaged by the unfolding story, seasonal tension, and the addictive pull of uncovering forbidden knowledge.

---

## 2. Core Gameplay Loop

```
IDLE: Resources accumulate offline
  ↓
CHECK-IN: Player collects harvest, taps drills, reads new lore fragment
  ↓
BUILD: Spend resources on buildings / research
  ↓
ADVANCE: Gain XP → level up → unlock new content
  ↓
MYSTERY: Decrypt Arcanum fragment → progress secret society rank
  ↓
TENSION: Catastrophe Forecast Meter inches forward (shared, server-driven)
  ↓
DECISION: Vault resources or invest in current cycle?
  ↓  (season end)
CATASTROPHE: Partial reset, prestige bonuses, new biome unlocks
  ↓
[Repeat with more depth]
```

---

## 3. Resource System

### Tier 1 — Surface Resources (unlock: Day 1)

| Resource | Real analog | Used for |
|----------|-------------|---------|
| Iron | Iron ore | Basic tools, shelter frames |
| Copper | Copper ore | Wiring, electronics scaffolding |
| Coal | Coal seam | Fuel for forges |
| Limestone | Limestone | Construction, mortar |
| Clay | Surface clay | Ceramics, early storage vessels |

### Tier 2 — Deep Crust (unlock: Civ Level 5)

| Resource | Real analog | Used for |
|----------|-------------|---------|
| Gold | Gold vein | Vault reserves, trade currency |
| Silver | Silver vein | Arcanum ritual items, conductors |
| Crude Oil | Petroleum | Generators, vehicle fuel |
| Rare Earth | Rare earth minerals | Advanced electronics |
| Quartz | Quartz crystal | Timepieces, signal amplifiers |

### Tier 3 — Mantle (unlock: Arcanum Rank 3+)

| Resource | Real analog | Used for |
|----------|-------------|---------|
| Osmium | Osmium deposits | Alloy for Vault armor |
| Titanium | Titanite | Catastrophe-resistant structures |
| Helium-3 | He-3 pockets | Advanced energy research |
| Obsidian | Volcanic glass | Arcanum ceremonial items |
| Iridium | Iridium vein | Pre-catastrophe tech reconstruction |

### Tier 4 — Gnosis Resources (Knowledge Mining)

| Resource | Description | Used for |
|----------|-------------|---------|
| Cipher Fragments | Encrypted text pieces | Decoded for lore + Gnosis XP |
| Arcanum Seals | Symbol sets | Unlock Arcanum rank gates |
| Prophecy Shards | Scattered predictions | Assemble for Catastrophe forecasts |
| Ley Maps | Ancient cartography | Unlock hidden biome zones |

---

## 4. Building System

### Infrastructure Buildings

| Building | Resource Cost | Effect |
|----------|--------------|--------|
| Basic Shelter | 50 Iron, 30 Clay | +5% offline mining rate |
| Forge | 100 Iron, 50 Coal | Enables metal refining |
| Deep Drill | 200 Iron, 100 Copper | Unlocks Tier 2 resources |
| Research Lab | 150 Rare Earth, 50 Gold | Enables tech tree research |
| Signal Tower | 100 Quartz, 75 Silver | Receives Arcanum transmissions |
| Vault | 500 Gold, 200 Titanium | Stores resources through Catastrophe |
| Observatory | 300 Quartz, 100 Obsidian | Accelerates Prophecy Shard decay |

### Catastrophe Bunker (Prestige Building)
- Costs: 1,000 Titanium + 200 Iridium + 5 Arcanum Seals
- Effect: Doubles Vault capacity; survives Catastrophe with 100% integrity
- One per season; carries over as permanent structure (appearance only)

---

## 5. Arcanum Secret Society — Rank System

The Arcanum has existed since before the catastrophe. They know what caused it. They know the next one is coming.

| Rank | Name | Unlock Gate | Reward |
|------|------|------------|--------|
| 0 | Uninitiated | Starting | Basic lore entry |
| 1 | Seeker | 3 Cipher Fragments | +10% Knowledge Mining speed |
| 2 | Initiate | Signal Tower built | Arcanum dialogue begins |
| 3 | Adept | 10 Gnosis XP | Access to Mantle biome |
| 4 | Scholar | 5 Arcanum Seals | Catastrophe Meter visibility |
| 5 | Sentinel | 1 full season survived | Vault bonus ×1.5 |
| 6 | Keeper | Assemble full Prophecy | True ending story chapter |
| 7 | Sovereign | All resources Tier 4 unlocked | Prestige cosmetic: "Sovereign" title, golden mine skin |

---

## 6. Catastrophe System

### Mechanics
- **Forecast Meter**: A global server-side meter (Firebase). All players' mining activity contributes to it filling. When full → Catastrophe fires at end of current season cycle.
- **Personal Meter**: Player can see their individual contribution and the global total.
- **Warning Phase**: Final 48 hours before Catastrophe — notifications, in-game alerts, escalating ambient music.

### Catastrophe Reset Rules
| Item | Fate after Catastrophe |
|------|----------------------|
| Building levels | Reset to 0 |
| Raw resources | Lost (unless Vaulted) |
| Vaulted resources | Survive (up to Vault capacity) |
| Arcanum rank | **Permanent — never resets** |
| Gnosis XP | **Permanent — never resets** |
| Cosmetics | **Permanent** |
| Arcanum Pass rewards | **Permanent** |
| XP Level | Partial reset (keep 25% of level progress) |

### Post-Catastrophe Unlocks
- New biome layer (each season introduces one new zone)
- New resource type (one per catastrophe cycle)
- New lore chapter ("The Arcanum Reveals…")
- Exclusive cosmetic for survivors

---

## 7. XP & Leveling

### XP Sources
| Action | XP Reward |
|--------|----------|
| Passive mining tick | 1–10 (scales with tier) |
| Manual drill tap | 25 |
| Building constructed | 100–500 (scales with building tier) |
| Daily login | 50 × streak day (cap: day 14) |
| Weekly login bonus | 300 |
| Knowledge fragment decrypted | 200 |
| Arcanum rank gained | 500 |
| Expedition completed | 150–400 |
| Catastrophe survived | 1,000 |
| Season Arcanum Pass holder bonus | +20% all XP that season |

### Level Milestones
| Level | Unlock |
|-------|--------|
| 5 | Deep Crust biome access |
| 10 | Expedition system |
| 15 | Signal Tower buildable |
| 20 | Arcanum puzzle system |
| 30 | Second Vault slot |
| 50 | Mantle biome access |
| 100 | "Sovereign" title eligibility |

---

## 8. Return Hooks & Engagement

| Hook | Mechanism |
|------|----------|
| **Offline progress** | Resources accumulate while away (up to 8 hours) |
| **Daily streak** | Login streak multiplier; breaking streak costs nothing but resets multiplier |
| **Expedition timer** | 2–8 hour timed missions; push notification on completion |
| **Catastrophe Forecast** | "The meter is at 67% — be online when it hits 100%" |
| **Arcanum transmissions** | Random lore fragments delivered like push notifications at odd hours (creepy/immersive) |
| **Weekly puzzle drop** | New cipher fragment every Tuesday at reset |
| **Leaderboard** | "Top 100 Survivors this season" public ranking by Vault wealth |
| **Seasonal battle pass milestone** | Visual progress bar always visible; next reward always close |

---

## 9. Monetization Details

### Arcanum Pass (Seasonal Battle Pass) — $4.99/season
- 40 reward tiers (cosmetics, titles, minor accelerators)
- Tier 1–20: Free track (available to all)
- Tier 21–40: Paid track (Arcanum Pass holders)
- Never gates story or gameplay systems
- Pass expires at Catastrophe; new pass issued next season

### In-App Purchases
| Item | Price | Description |
|------|-------|-------------|
| Vault Expansion I | $0.99 | +200 resource slots |
| Vault Expansion II | $1.99 | +500 resource slots |
| Vault Expansion III | $2.99 | +1,000 resource slots (max) |
| Boost Pack | $0.49 | Skip one 4-hour expedition timer |
| Mine Skin: Ember | $0.99 | Visual reskin for mining view |
| Mine Skin: Frost | $0.99 | Visual reskin |
| Settlement Skin: Obsidian | $1.99 | Full settlement visual overhaul |
| Arcanum Frame Pack | $1.49 | 5 profile frame cosmetics |

### Rewarded Ads (Voluntary)
- Player taps "Watch to Boost" button (never auto-shown)
- Reward: ×2 next harvest or +30 min expedition reduction
- Limit: 3 rewarded ads per day
- No interstitials during gameplay. Period.

---

## 10. Technical Architecture

### Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture (UseCases)
- **Database**: Room (offline progress, resource state)
- **Networking**: Retrofit + OkHttp (Arcanum server transmissions, leaderboard)
- **Backend**: Firebase (Realtime DB for Catastrophe Meter, Auth, Analytics, Remote Config)
- **IAP**: Google Play Billing Library v6
- **Ads**: Google AdMob (rewarded only)
- **Notifications**: Firebase Cloud Messaging (FCM)
- **DI**: Hilt

### Module Structure (planned)
```
:app                    — Android application entry point
:feature:mining         — Resource extraction engine
:feature:knowledge      — Gnosis/cipher puzzle system
:feature:civilization   — Building and city management
:feature:arcanum        — Secret society rank and lore
:feature:catastrophe    — Forecast meter and season management
:feature:vault          — Resource storage and prestige carry-over
:core:database          — Room entities and DAOs
:core:network           — API clients
:core:domain            — Business logic / UseCases
:core:ui                — Shared Compose components, theme
```

### Offline Progress Formula
```
resources_gained = base_rate × time_offline_seconds × efficiency_multiplier
time_cap = 8 * 3600  // 8 hours max offline accumulation
```

---

## 11. Aesthetic Direction

- **Color palette**: Deep charcoal (#0D0D0D), aged gold (#C9A84C), crimson ember (#8B1A1A), icy silver (#C0C0D4)
- **Typography**: Serif for lore/Arcanum text; clean sans-serif for UI numbers
- **Sound**: Ambient cave drips, pickaxe impacts, distant rumbles; orchestral swell on Arcanum reveals
- **Animations**: Particle effects for resource collection; earthquake shake on Catastrophe approach
- **UX principle**: Every screen should tell part of the story even in silence

---

*Document maintained by: Sovereign Interactive / jru727ab-cpu*
