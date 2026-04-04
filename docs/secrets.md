# CIVILTAS — Secrets System

> *"Knowledge is the only currency that survives a catastrophe."*

---

## Overview

**Secrets** are the narrative and progression backbone of CIVILTAS. They represent fragments of hidden knowledge that civilizations fought to protect — or bury — before the last catastrophe. Players discover, earn, and optionally purchase these fragments to gain strategic advantages, unlock new mechanics, and piece together the story of what really happened.

Secrets are **never pay-to-win**. Paid secrets offer information, narrative context, convenience, or cosmetic unlocks — never raw power that a free player cannot eventually earn through gameplay.

---

## Categories

### 1. Lore Secrets
*What happened. Who did it. Why it was hidden.*

Fragments of the historical record surrounding the catastrophic event. These tell the human story: political decisions, cover-ups, martyrs, and the factions who knew. They deepen narrative immersion and unlock special dialogue or story chapters.

| Effect | Notes |
|--------|-------|
| Unlock story chapter | New cutscene / text log |
| Reveal faction background | Affects NPC trust ratings |
| Unlock Codex entry | Permanent collectible |

### 2. Survival Intel Secrets
*Where to go. What to avoid. How long you have.*

Tactical knowledge about the environment: safe migration corridors, contamination zones, weather patterns, underground aquifers. Survival Intel secrets give real in-game advantages while remaining earnable through play.

| Effect | Notes |
|--------|-------|
| Reveal safe zone on map | Reduces hazard penalty |
| Unlock migration route | Faster expedition travel |
| Reduce hazard damage % | Passive modifier |

### 3. Resource Intel Secrets
*Where the best deposits are. Which methods actually work.*

Knowledge of resource-rich locations, extraction techniques, and trade routes that survived the catastrophe. These directly improve gathering efficiency.

| Effect | Notes |
|--------|-------|
| Reveal high-yield deposit | Map icon + bonus output |
| Unlock advanced extraction method | Production rate bonus |
| Discover lost trade route | Unlock rare resource node |

### 4. Sacred Geometry / Ancient Knowledge Secrets
*How to build things that shouldn't be buildable. Forgotten science.*

The most powerful category — these unlock entirely new building blueprints, advanced tech tiers, and crafting recipes that pre-date current understanding. Cannot be reverse-engineered without this knowledge.

| Effect | Notes |
|--------|-------|
| Unlock advanced building blueprint | New structure type |
| Unlock tech tier | Gates a research branch |
| Unlock crafting recipe | New item or module |

### 5. Society Rank Secrets
*Who you are. What you are allowed to know.*

Secrets tied to social standing — passwords, oaths, symbols, and rank insignia of pre-catastrophe organisations. These gate high-level NPC interactions, faction quests, and multiplayer features like guilds/alliances.

| Effect | Notes |
|--------|-------|
| Unlock NPC faction dialogue | New quests available |
| Raise faction standing | Permanent rep gain |
| Unlock alliance feature | Multiplayer/social gate |

---

## Rarity / Tiers

| Tier | Name | Description | Earn path | Purchasable? |
|------|------|-------------|-----------|-------------|
| 1 | **Common** | Basic historical fragments; minimal gameplay impact | Daily tasks, quests | No (earn only) |
| 2 | **Uncommon** | Practical intel with noticeable bonuses | Expeditions, research | No (earn only) |
| 3 | **Rare** | Significant mechanic unlocks; moderate gameplay effect | Research milestones, season pass (free track) | Convenience purchase (faster unlock, same secret) |
| 4 | **Epic** | Major blueprint/tech unlocks; strong narrative beats | Long expeditions, season pass (premium track) | Yes (info/convenience, not raw power) |
| 5 | **Legendary** | Unique story-changing secrets; game-altering knowledge | End-game, catastrophe cycle completion | Cosmetic/collector purchase only |

> **Guardrail**: Tiers 4–5 purchased via IAP are **identical** to the earnable versions — the purchase skips wait time or grind, it does not grant a superior secret.

---

## Earning Secrets (Free Paths)

All secrets have a free earn path. Paid access is always a **convenience shortcut**, never an exclusive advantage.

### Quests / Daily Tasks
- Complete daily objectives → earn Common/Uncommon secrets
- Complete story quests → earn narrative Lore and Survival Intel secrets
- Streak bonus (7-day streak) → guaranteed Rare secret

### Expeditions
- Short expeditions (1–4h): chance of Uncommon/Rare secrets
- Long expeditions (8–24h): higher chance, includes Epic tier
- Expedition type affects secret category (e.g., Cave Expedition → Resource Intel)

### Research Milestones
- Complete a full research branch → unlock a thematically relevant Secret
- Sacred Geometry unlocks gate Ancient Knowledge secrets specifically
- Research cannot be purchased; only time/resource investment earns these

### Catastrophe Cycle Events
- Mini-events during the catastrophe forecast window → emergency Intel secrets
- Completing a full cycle (surviving a catastrophe) → Legendary secret

---

## Purchasing Secrets (Monetization — Non-P2W)

### What you can buy
| Purchase type | What you get | What you don't get |
|---------------|-------------|-------------------|
| **Early unlock** | Access a Rare/Epic secret now instead of in 48h | A stronger secret than earnable |
| **Season Pass (Premium)** | Premium track secrets (cosmetic + narrative) earlier | Raw power over free players |
| **Cosmetic bundle** | Visual skin for your Secrets Library, animated unlock effects | Any gameplay advantage |
| **Lore pack** | Full story chapter + linked secrets revealed | Gameplay advantage |
| **Convenience pack** | 3-day expedition cooldown removed for 1 secret | More secrets than earnable limit |

### What you cannot buy
- A higher-tier secret than is currently earnable
- Bypassing research milestones (research is always time/resource)
- Society Rank secrets without meeting the in-game rank prerequisite

### Billing Integration (Stub)
Purchases are routed through a `BillingProvider` interface. The current implementation is a local-receipt stub (offline-friendly). When ready, swap `StubBillingProvider` for `PlayBillingProvider` (Google Play) or `StripeBillingProvider` (direct APK). See `src/secrets/SecretsBillingStub.kt`.

---

## Pacing & Drip-Feed Design

To prevent boredom and maintain suspense:

1. **Daily limit**: No more than 3 secrets unlocked per day from any source (prevents binge and maintains mystery)
2. **Discovery delay**: Epic and Legendary secrets take 24–48h to "decrypt" after being found (real-time, offline-friendly)
3. **Catastrophe window tension**: As the next catastrophe forecast rises, certain secrets become temporarily locked ("data corrupted — atmospheric interference"), creating urgency
4. **Rotating spotlight**: Each week, 1–2 secrets are highlighted as "rumoured" (visible but locked), creating FOMO and discovery goals

---

## Secrets Library UI

The Secrets Library is a dedicated screen in the app where players:
- Browse discovered secrets (filterable by category and tier)
- See locked secrets as silhouettes with a hint ("A map fragment hinting at safety…")
- Unlock earned secrets with a reveal animation
- View purchase options for convenience/early unlocks
- Track their progress toward earning each secret

See `index.html` for the interactive prototype and `src/secrets/` for the Kotlin implementation stubs.
