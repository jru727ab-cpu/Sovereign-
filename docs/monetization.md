# Monetization Strategy — Sovereign

This document defines the monetization approach for Sovereign, including product categories, pricing philosophy, implementation guardrails, and anti-patterns to avoid.

---

## Table of Contents

1. [Philosophy & Guardrails](#philosophy--guardrails)
2. [Revenue Streams](#revenue-streams)
   - [Rewarded Advertising](#1-rewarded-advertising)
   - [In-App Purchases — Cosmetics](#2-in-app-purchases--cosmetics)
   - [In-App Purchases — Convenience Boosts](#3-in-app-purchases--convenience-boosts)
   - [Season Pass](#4-season-pass)
   - [Sovereign Pass (Subscription)](#5-sovereign-pass-subscription)
3. [In-Game Currencies](#in-game-currencies)
4. [Pricing Tiers](#pricing-tiers)
5. [Anti-Patterns to Avoid](#anti-patterns-to-avoid)
6. [Revenue Projections (Illustrative)](#revenue-projections-illustrative)

---

## Philosophy & Guardrails

Sovereign should feel fair. Monetization must enhance the experience — never gate core fun behind a paywall.

### Hard Rules
1. **No cryptocurrency.** No blockchain, no NFTs, no crypto wallets, no real-money trading of in-game items. All in-game currencies are virtual and non-transferable.
2. **No pay-to-win for competitive content.** Paying players must not gain a meaningful advantage in Catastrophe Cycle rankings or leaderboards over free players who invest equivalent time.
3. **All gameplay-affecting items must be earnable.** Any boost, building slot, or resource cache available for purchase must also be obtainable through gameplay (quest rewards, event drops, etc.). Paying only accelerates, not gates.
4. **No loot boxes with hidden odds for gameplay items.** Cosmetic mystery boxes are acceptable only if odds are disclosed. No random drops for gameplay-affecting items.
5. **No aggressive monetization toward minors.** If the game is available to users under 18, in-app purchases must respect platform requirements (parental controls, spending caps).

---

## Revenue Streams

### 1. Rewarded Advertising

**Format:** Opt-in, player-initiated. Never interrupt gameplay with forced ads.

| Placement | Reward | Max per day |
|---|---|---|
| Offline bounty collection screen | Double offline resources (one-time) | 1× |
| After completing a building | Instant 50% construction time reduction | 2× |
| Daily quest screen | +50% XP on next completed quest | 2× |
| Resource shortage prompt | Small emergency resource cache | 3× |

**Implementation notes:**
- Use a reputable ad SDK (e.g., AdMob, Unity Ads, IronSource).
- Rewarded ads should never auto-play.
- Players may dismiss the offer at any time with no penalty.
- Sovereign Pass subscribers see reduced ad offer prompts (they still exist, but are surfaced less frequently).

**Revenue model:** CPM/CPC revenue share with ad network.

---

### 2. In-App Purchases — Cosmetics

Cosmetics are purely visual. They provide no gameplay advantage.

**City & Building Skins**
| Item | Price (USD) |
|---|---|
| Ancient Ruins city theme | $1.99 |
| Volcanic Forge building set | $2.99 |
| Crystal Cavern mine skin | $1.99 |
| Bundle: Founding Era Starter Pack | $4.99 |

**Avatar Frames & Titles**
| Item | Price (USD) |
|---|---|
| Survivor Frame | $0.99 |
| Illuminated Frame | $1.99 |
| Sovereign Title Bundle (5 titles) | $2.99 |

**Drill Rig & Resource Node Skins**
| Item | Price (USD) |
|---|---|
| Steampunk Rig skin | $1.99 |
| Crystal Mine node skin | $1.49 |

**Cosmetic Bundles**
- Launch Bundle: city skin + frame + 3 titles → $5.99 (saves ~40%)
- Catastrophe Survivor Pack (post-event exclusive): $3.99

---

### 3. In-App Purchases — Convenience Boosts

Boosts accelerate progress but do not create a permanent advantage. All boosts are also earnable through quests and events.

| Boost | Effect | Duration | Price (USD) | Earnable via |
|---|---|---|---|---|
| Mining Rush | 2× tap output | 1 hour | $0.99 | Weekly quest |
| Builder's Haste | 50% faster construction | 4 hours | $1.99 | Catastrophe event reward |
| Scholar's Focus | 2× research speed | 2 hours | $1.49 | Story quest |
| Vault Overflow | +25% storage cap | 48 hours | $0.99 | Achievement reward |
| Resource Cache (Small) | Instant resource bundle | One-time | $0.99 | Daily quest, level-up reward |
| Resource Cache (Large) | Larger instant bundle | One-time | $2.99 | Catastrophe survival reward |

**Design note:** Boosts should never be required to progress. The free path should feel satisfying; boosts should feel like a welcome shortcut, not a necessity.

---

### 4. Season Pass

A seasonal content drop tied to the narrative Catastrophe Cycle (roughly every 90 days).

**Structure:**
- **Free track**: available to all players; basic XP bonuses and resource rewards.
- **Paid track**: $4.99 one-time purchase per season; exclusive cosmetics, bonus Gnosis Shards, and accelerated Season Pass XP.

**Season Pass rewards are never required** for story progression or competitive play. They are cosmetic and convenience bonuses only.

**Example: Season 1 — "The First Tremor"**
- Free track: 20 tiers of resource caches and XP boosts
- Paid track (unlocks all 40 tiers): +20 tiers with city skin, animated flag, Gnosis Shard pack, exclusive "First Tremor" title

---

### 5. Sovereign Pass (Subscription)

An optional monthly/annual subscription for players who want ongoing QoL benefits.

| Feature | Sovereign Pass |
|---|---|
| Daily Gnosis Shard bonus | +2 per day |
| Reduced ad prompts | Yes (still opt-in available) |
| Offline accumulation window | 12 hours (vs 8 hours free) |
| Exclusive cosmetic drops | Monthly exclusive frame/title |
| Vault storage bonus | +10% permanent while subscribed |
| Early access to Season Pass content | 1 week early |

**Pricing:**
- Monthly: $4.99/month
- Annual: $39.99/year (~33% savings)

**Important:** All Sovereign Pass benefits lapse if the subscription is cancelled. No progress is lost — only the QoL and cosmetic bonuses revert.

---

## In-Game Currencies

Sovereign uses two in-game currencies to clearly separate earnable from purchasable value.

| Currency | Name | Source | Use |
|---|---|---|---|
| **Hard currency** | Gnosis Shards | Purchased, rare quest/event rewards | Premium cosmetics, boost packs, Season Pass paid track |
| **Soft currency** | Ingots | Earned through play | Standard buildings, upgrades, basic research |

**Gnosis Shard IAP Packs:**
| Pack | Shards | Price (USD) | Bonus |
|---|---|---|---|
| Small | 100 | $0.99 | — |
| Medium | 550 | $4.99 | +10% bonus |
| Large | 1 200 | $9.99 | +20% bonus |
| Vault | 2 600 | $19.99 | +30% bonus |
| Sovereign Stockpile | 7 000 | $49.99 | +40% bonus |

**Design note:** Gnosis Shards should never be the only path to a gameplay-affecting item. Farmable Ingots must cover all core progression needs.

---

## Pricing Tiers

Sovereign targets three payer segments:

| Segment | Spend / Month | Products |
|---|---|---|
| **Non-payer** | $0 | Rewarded ads, full gameplay via time |
| **Light payer (minnow)** | $1–$5 | Occasional cosmetic, boost pack, small Shard pack |
| **Medium payer (dolphin)** | $5–$20 | Sovereign Pass, Season Pass, medium Shard pack |
| **High payer (whale)** | $20+ | Annual pass, large Shard packs, full cosmetic library |

The game must be fully enjoyable for non-payers. Monetization is designed to convert through desire (cosmetics, convenience) not frustration.

---

## Anti-Patterns to Avoid

The following patterns should **never** be implemented:

| Anti-Pattern | Why |
|---|---|
| Energy/stamina gates on core mining | Creates frustration; players feel punished for playing |
| Pay to remove ads (forced interstitials) | Only rewarded/opt-in ads are used |
| Loot boxes for gameplay items | Gambling-adjacent; regulatory risk |
| Cryptocurrency / NFT integration | Explicitly out of scope; regulatory risk; damages brand trust |
| Exclusive gameplay content only for paying players | Breaks the fairness promise |
| Increasing prices via artificial scarcity (e.g., "only 3 left!") | Deceptive practice |
| Real-money auctions or player-to-player trading | Creates pay-to-win and regulatory risk |
| Automatic recurring charges without clear disclosure | Legal liability; bad UX |

---

## Revenue Projections (Illustrative)

> These are rough illustrative estimates for planning purposes only. Actual results depend heavily on user acquisition, retention, and market conditions.

Assuming 10 000 Monthly Active Users (MAU):

| Stream | Est. Conversion | Est. Monthly Revenue |
|---|---|---|
| Rewarded ads | 60% of DAU watch ≥1 ad/day | $200–$600 |
| Cosmetic IAP | 2% of MAU | $400–$800 |
| Boost IAP | 3% of MAU | $300–$600 |
| Season Pass | 5% of MAU | $250–$500 |
| Sovereign Pass | 2% of MAU | $100–$300 |
| **Total (low–high)** | | **$1 250 – $2 800 / month** |

To scale revenue meaningfully, user acquisition and strong retention (target D7 > 25%, D30 > 10%) are the primary levers. The XP and progression systems in [progression.md](progression.md) are designed to support those retention targets.
