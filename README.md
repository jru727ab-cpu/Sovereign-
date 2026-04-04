# Sovereign — Rebuild. Discover. Survive.

> **Status: Early Development / Work In Progress**

Sovereign is a mobile-first idle/strategy game where players lead a fledgling civilization that has just survived a world-ending catastrophe. Mine real-earth resources, construct buildings, unearth ancient knowledge — and prepare for the catastrophe that is already on its way.

---

## Table of Contents

1. [Concept](#concept)
2. [Core Gameplay Loops](#core-gameplay-loops)
3. [XP & Progression](#xp--progression)
4. [Monetization](#monetization)
5. [Roadmap](#roadmap)
6. [Development Setup](#development-setup)
7. [Contributing](#contributing)

---

## Concept

The world ended. Almost.

A small group of survivors — your civilization — clawed their way out of the rubble. Resources are scarce. The land must be excavated for metals, crude oil, and precious minerals. Knowledge of the old world must be rediscovered piece by piece.

But a **Secret Society** already knows what comes next. They possess ancient Gnosis: forbidden records of past catastrophes that run in cycles. The next event is not centuries away — it is closer than anyone dares admit.

Players face a constant tension:

| Build for Now | Prepare for Next |
|---|---|
| Expand cities and infrastructure | Stockpile rare resources |
| Grow your population | Fortify your vaults |
| Pursue daily comfort | Seek out the Secret Society |

The choices you make today shape whether your civilization merely survives — or transcends.

---

## Core Gameplay Loops

See [docs/gameplay.md](docs/gameplay.md) for full details.

### Resource Mining
- Tap/idle-mine surface deposits of **iron, copper, gold, silver, crude oil, rare earth elements**, and more.
- Deeper drills and upgraded rigs unlock rarer materials.
- Resources are used for construction, research, and trade.

### Building & Infrastructure
- Construct Foundries, Refineries, Academies, Vaults, and Sanctuaries.
- Buildings increase passive resource production and unlock new game content.
- Balance population needs vs. war-chest hoarding.

### Knowledge & Gnosis
- Research **Tech Trees** to unlock science-based upgrades.
- Discover hidden **Gnosis Fragments** through exploration events and Secret Society quests.
- Gnosis unlocks the meta-narrative: the true history of the catastrophe cycle.

### The Catastrophe Cycle
- Periodic **world events** test your civilization's readiness.
- Players who stockpiled and built defenses survive with bonuses.
- Players who ignored warnings face resource penalties and narrative consequences.
- The cycle resets, raising stakes each time.

---

## XP & Progression

See [docs/progression.md](docs/progression.md) for full details.

| Source | XP Reward |
|---|---|
| Mine a resource node | 10–50 XP |
| Complete a building | 100–500 XP |
| Finish a quest | 200–1 000 XP |
| Daily login streak | 50–500 XP (streak bonus) |
| Survive a catastrophe event | 1 000–5 000 XP |
| Unlock a Gnosis Fragment | 250 XP |

**Offline / Idle Progress**: resources and low-level XP continue to accumulate while the app is closed. Players return to collect their bounty, reinforcing a daily check-in habit.

**Prestige / Rebirth**: after reaching the level cap, players can "Prestige" — resetting their civilization but carrying permanent bonuses and exclusive cosmetics into the next cycle.

---

## Monetization

See [docs/monetization.md](docs/monetization.md) for full details.

**Guardrails:** No cryptocurrency. No pay-to-win mechanics that break game balance. All gameplay-affecting items must be earnable through play.

| Category | Examples |
|---|---|
| Cosmetics (IAP) | City skins, drill rigs, flag designs, avatar frames |
| Boost Packs (IAP) | Temporary 2× production boosts (available through gameplay too) |
| Optional Subscription | "Sovereign Pass" — cosmetics + QoL features, no power advantage |
| Rewarded Ads | Watch an ad to double a single mining run |
| Season Pass | Themed content drops tied to the catastrophe cycle narrative |

---

## Roadmap

### MVP (v0.1 — v0.5)
- [x] Basic HTML prototype shell (`index.html`)
- [ ] Core resource mining loop (tap + idle)
- [ ] 5 starting resources (iron, copper, gold, oil, stone)
- [ ] Basic building system (Foundry, Mine Shaft, Academy)
- [ ] XP bar and first 20 levels
- [ ] Daily login streak tracker
- [ ] Local data persistence (localStorage / SQLite)

### Early Access (v0.6 — v1.0)
- [ ] Knowledge / Tech Tree (10 initial nodes)
- [ ] First catastrophe event
- [ ] Gnosis Fragment system (narrative unlocks)
- [ ] Secret Society quest chain (5 quests)
- [ ] Rewarded ads integration
- [ ] Basic cosmetic shop

### Post-Launch (v1.x+)
- [ ] Multiplayer / alliances
- [ ] Expanded catastrophe cycle (3 unique event types)
- [ ] Prestige / Rebirth system
- [ ] Sovereign Pass subscription
- [ ] Season Pass (narrative-driven content drops)
- [ ] Push notifications for offline progress
- [ ] Leaderboards

---

## Development Setup

> The project is in early development. There is currently no build pipeline — the prototype runs as a static HTML file.

### Requirements
- Any modern web browser (Chrome, Firefox, Safari)
- A local web server (optional but recommended to avoid CORS issues)

### Running Locally

```bash
# Clone the repository
git clone https://github.com/jru727ab-cpu/Sovereign-.git
cd Sovereign-

# Option 1 — open directly in browser
open index.html

# Option 2 — serve with Python (recommended)
python3 -m http.server 8080
# Then open http://localhost:8080 in your browser

# Option 3 — serve with Node.js
npx serve .
```

### Repository Structure

```
Sovereign-/
├── index.html          # Prototype UI shell
├── README.md           # This file
└── docs/
    ├── gameplay.md     # Detailed gameplay loop design
    ├── progression.md  # XP & level system design
    └── monetization.md # Monetization strategy & guardrails
```

---

## Contributing

This project is in active early design. If you have ideas for:
- Game mechanics
- Narrative content
- Art direction
- Technical architecture

Please open an Issue or start a Discussion in this repository.

---

*Sovereign — Every civilization thinks it will be the last. Prove them right.*
