# SOVEREIGN — Android Idle Civilization Mining Game

> *You are a survivor. Resources fuel your rise. Knowledge is your weapon. A second catastrophe is coming.*

---

## 📋 Repository Recommendation

**Continue in this repository.** The repo name *Sovereign-* perfectly fits the game's identity. The existing stub files (`index.html`) are just early experiments that can be kept or removed — they carry no weight. Starting fresh in a new repo would only add friction (new clone, broken links, lost CI config). All Android project scaffolding has been added to this repo directly.

---

## 🎮 Game Concept

**SOVEREIGN** is an Android idle/incremental game where players rebuild civilization after a catastrophic world-ending event — while secretly racing against the clock before the *next* one. Unlike crypto-mining clones, you mine the **physical earth** (metals, minerals, oil, rare elements) and, separately, **forbidden knowledge** from a hidden secret society who already knows the truth.

### Core Pillars

| Pillar | Description |
|--------|-------------|
| **Earth Mining** | Idle extraction of 20+ real-world resources across three layers: Surface, Deep Crust, Mantle |
| **Knowledge Mining** | Decrypt fragments, solve riddles, and unlock Gnosis tiers from the secret society (The Arcanum) |
| **Civilization Building** | Spend resources to build infrastructure (shelters, forges, research labs, vaults) |
| **Catastrophe Clock** | A hidden countdown drives tension — hoard for the next event or build for the present? |
| **Secret Society Ranks** | Progress through 7 Arcanum ranks to unlock lore, bonuses, and hidden game mechanics |

---

## 🏗️ Project Structure

```
Sovereign-/
├── app/                        # Android application module
│   ├── build.gradle            # App-level Gradle config
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/sovereign/civiltas/
│           │   └── MainActivity.kt   # Compose entry point
│           └── res/
│               ├── values/
│               │   ├── strings.xml
│               │   ├── colors.xml
│               │   └── themes.xml
│               └── drawable/         # Icons and graphics
├── build.gradle                # Root Gradle config
├── settings.gradle             # Module settings
├── gradle.properties           # JVM / build flags
├── gradlew / gradlew.bat       # Gradle wrapper scripts
├── GAME_DESIGN.md              # Full game design document
└── README.md                   # This file
```

---

## 🚀 Getting Started (Developers)

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34 (compile), minSdk 26 (Android 8.0+)

### Build & Run

```bash
# Clone
git clone https://github.com/jru727ab-cpu/Sovereign-.git
cd Sovereign-

# Build debug APK
./gradlew assembleDebug

# Install on connected device / emulator
./gradlew installDebug

# Run unit tests
./gradlew test
```

---

## 📈 XP & Progression System

Players never run out of things to do. XP flows from multiple independent sources:

| Source | XP / tick | Notes |
|--------|-----------|-------|
| Passive mining | +1–10 | Auto-runs offline; rate depends on tier |
| Manual drilling | +25 | Tap action with cooldown |
| Building construction | +100–500 | One-time per building |
| Daily login streak | +50 × streak_day | Day 7 gives ×3 bonus |
| Knowledge fragment decrypted | +200 | Rare, tied to puzzle system |
| Expedition completed | +150–400 | Timed missions |
| Catastrophe survived | +1,000+ | Season milestone |

**Level milestones** unlock new biomes, Arcanum rank upgrades, cosmetic titles, and building blueprints.

---

## 💰 Monetization (Respectful by Design)

No pay-to-win. No intrusive ad walls. Revenue comes from:

| Model | Details |
|-------|---------|
| **Arcanum Pass** (seasonal) | $4.99/season (~8 weeks). Cosmetic rewards, +15% offline earnings multiplier, exclusive lore chapters. Does *not* gate gameplay. |
| **Vault Expansions** | One-time IAP ($0.99–$2.99) to expand resource storage slots. QoL only. |
| **Cosmetic Packs** | Avatar frames, settlement skins, mine visual themes. $0.99–$1.99 each. |
| **Rewarded Ads** | *Optional* — player-triggered only. Doubles next harvest (up to 3×/day). Never forced. |
| **Accelerator Boosts** | Small IAP ($0.49) to skip a 4-hour timer once. Cannot stack infinitely. |

**Philosophy:** A player who never spends a dollar should feel equally welcomed. Spending rewards aesthetics and convenience — never access to story or competitive advantage.

---

## 🗓️ Catastrophe Cycle (Season Design)

Each season (~8 weeks) ends with a **Catastrophe Event** that partially resets the world:

- **Prestige Loop**: Players keep permanent bonuses (Arcanum rank, Gnosis points, cosmetics) but lose building levels
- **Tension builds**: The *Catastrophe Forecast Meter* fills publicly as all players mine — creating a collective countdown
- **Decision moment**: Hoard resources in the Private Vault (survives reset) or invest in current-cycle buildings for bigger in-cycle rewards?
- **Post-catastrophe**: New biome layers unlock, new resource types introduced, story chapter released

---

## 🔮 Next Steps

- [ ] Implement idle mining engine (Kotlin coroutines + Room database for offline progress)
- [ ] Design resource DAG (20 resources with crafting chains)
- [ ] Build knowledge puzzle system (cipher fragments, riddle sequences)
- [ ] Create Arcanum secret society dialogue + lore tree
- [ ] Implement Catastrophe Forecast Meter (shared via Firebase Realtime Database)
- [ ] Set up Google Play Billing Library v6 for IAP
- [ ] Integrate Firebase Analytics + Remote Config for live balancing
- [ ] Design UI in Jetpack Compose (dark theme, aged-stone aesthetic)
- [ ] Accessibility pass (content descriptions, font scaling)
- [ ] Beta via Google Play Internal Testing track

---

## 📖 Full Game Design

See [GAME_DESIGN.md](GAME_DESIGN.md) for the complete design document including resource tables, building blueprints, Arcanum rank details, and technical architecture decisions.

---

## 📄 License

All rights reserved — jru727ab-cpu. See `LICENSE` for details.
