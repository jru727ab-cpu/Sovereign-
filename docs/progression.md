# Progression System — Sovereign

This document defines the XP and level system designed to create meaningful daily retention loops.

---

## Table of Contents

1. [Design Goals](#design-goals)
2. [XP Sources](#xp-sources)
3. [Level Table (1–50)](#level-table-1-50)
4. [Level Rewards](#level-rewards)
5. [Offline / Idle Progress](#offline--idle-progress)
6. [Daily Login Streaks](#daily-login-streaks)
7. [Quests & Missions](#quests--missions)
8. [Prestige / Rebirth System](#prestige--rebirth-system)

---

## Design Goals

1. **Daily check-in habit** — players should feel rewarded every time they open the app.
2. **Visible progress** — the XP bar and level indicator should always be prominent.
3. **Multiple paths to XP** — active tappers, builders, explorers, and story-focused players all earn XP through their preferred playstyle.
4. **Offline accumulation** — players who can't play every day should still feel progress when they return.
5. **Long-term endgame** — Prestige gives max-level players a reason to keep playing.

---

## XP Sources

### Active Play

| Action | XP Reward | Notes |
|---|---|---|
| Mine a surface resource (tap) | 10–50 XP | Capped at 200 XP/day from pure tapping |
| Complete a building construction | 100–1 000 XP | Scales with building tier |
| Upgrade a building | 50–500 XP | Scales with upgrade level |
| Unlock a Tech Tree node | 150–800 XP | Scales with tech tier |
| Collect idle / passive resources | 10 XP per collection | Up to 5 collections/day |

### Quests & Events

| Source | XP Reward |
|---|---|
| Complete a Daily Quest | 300–600 XP |
| Complete a Weekly Quest | 1 000–2 500 XP |
| Complete a Story Quest | 500–3 000 XP |
| Unlock a Gnosis Fragment | 250 XP |
| Complete a Secret Society quest | 750–2 000 XP |

### Catastrophe Cycle Events

| Outcome | XP Reward |
|---|---|
| Participate in a Catastrophe event | 500 XP (baseline) |
| Survive with >50% buildings intact | +1 000 XP |
| Survive with all resources protected | +1 500 XP |
| Gnosis Readiness bonus (Secret Society warned you) | +1 000 XP |
| Maximum readiness (all bonuses) | 5 000 XP total |

### Social & Engagement Bonuses

| Source | XP Reward |
|---|---|
| Daily login (no streak) | 50 XP |
| 7-day streak | 500 XP bonus |
| 30-day streak | 2 000 XP bonus |
| First time sharing a milestone | 100 XP (one-time) |

---

## Level Table (1–50)

XP requirements follow a gentle exponential curve designed so early levels are fast (rewarding) and later levels provide a satisfying long-term grind.

| Level Range | XP to Next Level | Approx. Time (active player) |
|---|---|---|
| 1–10 | 500 – 2 000 | 1–3 days |
| 11–20 | 2 500 – 8 000 | 1–2 weeks |
| 21–30 | 10 000 – 30 000 | 2–4 weeks |
| 31–40 | 35 000 – 80 000 | 1–2 months |
| 41–50 | 100 000 – 250 000 | 2–4 months |

> **Note:** These are initial estimates. Tuning will be required after playtesting.

---

## Level Rewards

Every level grants at least one tangible reward to maintain a sense of achievement.

| Reward Type | Levels |
|---|---|
| Resource cache (iron, copper, gold, etc.) | Every level |
| Building slot unlock | Levels 5, 10, 15, 20 … |
| Tech Tree node unlock | Levels 8, 16, 24, 32, 40 |
| New mine tier unlocked | Levels 10, 20, 30, 40 |
| Cosmetic item (title, frame, skin) | Levels 5, 10, 15, 25, 35, 50 |
| Gnosis Shard bonus | Levels 15, 25, 35, 45, 50 |
| Vault storage increase | Levels 10, 20, 30, 40 |
| Secret Society access | Level 20 (Story gate) |
| Prestige unlock | Level 50 |

---

## Offline / Idle Progress

One of the most important retention mechanics in idle games is the **offline accumulation loop**:

1. Player closes app → passive buildings keep producing resources at a reduced rate.
2. Player reopens app → "Welcome back!" screen shows accumulated resources and XP.
3. Collecting the bounty is satisfying and immediately actionable.

### Rules
- **Offline production cap**: 8 hours of production accumulates while offline. After 8 hours the rate drops to 25% until capped at 24 hours total. This encourages at least one daily check-in.
- **Offline XP**: 5 XP per hour offline (up to 40 XP for 8 hours; capped at 120 XP for 24 hours).
- **Notifications**: Push notifications are sent when the storage cap is near, encouraging the player to log in and collect.
- **Boost exceptions**: Active production boosts (from IAP or ads) do not apply during offline periods — they only affect active sessions.

### Collection UX
The "collect offline bounty" moment should be a highlight of the daily session:
- Animated resource drop-in with satisfying sound design
- Summary card showing total resources and XP earned since last session
- Optional "Double with ad" rewarded ad placement on the collection screen

---

## Daily Login Streaks

Streaks are a core retention lever. They must reward consistency without punishing short gaps too harshly.

### Streak Structure

| Day | Reward |
|---|---|
| Day 1 | 50 XP + Stone cache |
| Day 2 | 75 XP + Iron cache |
| Day 3 | 100 XP + Copper cache |
| Day 4 | 150 XP + Coal cache |
| Day 5 | 200 XP + Oil cache |
| Day 6 | 300 XP + Gold cache |
| Day 7 | **500 XP + Gnosis Shard + cosmetic reward** |

After day 7 the cycle repeats with a **multiplier** that grows each week (Week 2 rewards are 1.25×, Week 3 are 1.5×, etc., capped at 3×).

### Streak Protection
- Players may "freeze" a streak once per week (earnable in-game or purchasable).
- Missing one day resets the streak to Day 1 but does not lose any permanent progress.

---

## Quests & Missions

Quests provide directed objectives that teach mechanics, drive the narrative, and deliver XP rewards.

### Daily Quests (3 active at a time, refresh every 24 hours)
Examples:
- "Mine 50 iron ore" → 300 XP
- "Collect from idle production 3 times" → 350 XP
- "Upgrade any building" → 400 XP
- "Unlock a Tech Tree node" → 450 XP

### Weekly Quests (1 active, refreshes Mondays)
Examples:
- "Complete 10 daily quests this week" → 1 500 XP + cosmetic
- "Survive a mini-catastrophe event" → 2 000 XP
- "Find 2 Gnosis Fragments" → 1 800 XP

### Story Quests (permanent, story-driven)
A linear chain of ~50 quests that deliver the main narrative. Completing the chain is required for full Gnosis access and unlocks the Prestige system. Each quest awards XP and at least one piece of lore.

### Achievement Badges
Permanent one-time achievements visible on the player profile:
- "First Vein" — mine your first gold node
- "Survivor" — survive your first Catastrophe event
- "Illuminated" — unlock 10 Gnosis Fragments
- "True Sovereign" — Prestige for the first time

---

## Prestige / Rebirth System

Once a player reaches **Level 50** and has survived at least one full Catastrophe Cycle, the **Prestige** option becomes available.

### What Prestige Does
- **Resets**: civilization progress, buildings, resources, and XP back to Level 1
- **Keeps**: cosmetics, Gnosis Fragment lore entries, achievement badges, story quest completions
- **Grants** (permanent, carry across all future cycles):
  - A "+X%" passive production multiplier (stacks with each Prestige)
  - Exclusive Prestige cosmetic (title, city skin, or avatar frame)
  - A permanent Gnosis Shard bonus on Gnosis Fragment finds

### Prestige Tiers

| Prestige | Production Bonus | Exclusive Reward |
|---|---|---|
| I | +10% all resources | "Reborn" title |
| II | +20% all resources | Prestige city skin (Bronze Era) |
| III | +35% all resources | Prestige city skin (Silver Era) |
| IV | +55% all resources | Prestige city skin (Gold Era) |
| V+ | +10% per additional Prestige | Unique animated avatar frame |

### Design Rationale
Prestige resets keep the game feeling fresh for long-term players while the permanent bonuses ensure each cycle is faster and more powerful than the last — reinforcing the fantasy of an ever-advancing civilization.
