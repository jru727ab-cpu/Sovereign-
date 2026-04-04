# CIVILTAS — Monetization Philosophy

## Core Principle: Respect the Player

CIVILTAS will never use monetization patterns that:
- Block progress behind a paywall
- Create artificial scarcity to force spending
- Use deceptive dark patterns (fake urgency, hidden costs)
- Show unskippable or intrusive ads
- Sell power that makes free players feel disadvantaged

---

## Phase 1: No Monetization

The initial release (v0.1.x) ships with **zero monetization**. The game is free, fully functional, and contains no ads or IAP.

BuildConfig flags allow monetization to be toggled on per-build:
```kotlin
buildConfigField("boolean", "ONLINE_SYNC_ENABLED", "false")
buildConfigField("boolean", "ADS_ENABLED", "false")
```

---

## Phase 2: Ethical Monetization Options

When monetization is introduced, it will follow these rules:

### ✅ Acceptable Models

#### Cosmetics
- Civilization themes (color palettes, icon sets)
- Title cards and badges
- Animated resource icons
- Custom mine/building skins

#### Quality of Life (Non-P2W)
- **Offline cap extension**: Buy an extended offline cap (e.g., 8h → 24h). The free cap is generous enough to play daily without this.
- **Ad removal**: One-time purchase to remove all optional ads
- **Extra save slots**: Multiple civilization saves

#### Season Pass
- Cosmetic rewards across a season
- Bonus daily quest rewards (resource quantities only, not Skill Points)
- Exclusive themes
- **Never includes power upgrades or Skill Points**

#### Optional Rewarded Ads
- Watch an ad for a 2× resource boost for 5 minutes
- Watch an ad for a one-time resource package
- Always optional, never required for progression

### ❌ Never Acceptable

- Selling Skill Points
- Selling Upgrade levels
- Energy systems that block play
- Pay-to-unlock story content
- Loot boxes / gacha
- Manipulative pricing tiers

---

## VIP Subscription

A VIP subscription may be offered in a later phase:

**Suggested benefits (~$2.99/month):**
- 2× offline resource cap
- Ad-free experience
- Exclusive cosmetic each month
- Early access to new features (cosmetic only)

**Does not include:**
- Any gameplay advantage
- Extra Skill Points or upgrade discounts

---

## Revenue Philosophy

The goal is a game that earns revenue because players **love it and want to support it**, not because they feel trapped or pressured. A player who plays for a year and never spends a dollar is a success — they're proof the core experience works. That reputation brings more players.

> "We would rather have 10,000 happy free players than 1,000 frustrated paying ones."
