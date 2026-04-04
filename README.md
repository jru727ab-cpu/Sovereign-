# CIVILTAS — Sovereign Command

> *Rebuild. Discover. Survive what's coming.*

CIVILTAS is an **offline-first idle/incremental civilisation game** for Android. You rise from the ashes of a destroyed world — mining resources, constructing buildings, researching lost technology, and piecing together the **Secrets** of what really happened. But another catastrophe is coming. The question is: will you be ready?

---

## Core Pillars

### ⛏️ Build & Progress
An addictive hybrid loop: short actions (upgrades, expeditions, discoveries) every few minutes, long idle accumulation (1–8 hours) when you're away. Mine resources, refine them, construct buildings, unlock tech, and expand your civilisation.

### 🔮 Secrets — Progression, Narrative & Monetization
**Secrets** are the central mechanic for progression, storytelling, and optional monetization. They are fragments of hidden knowledge that survived the last catastrophe.

Five categories of secrets await discovery:
- **Lore** — What happened. Who caused it. The truth behind the catastrophe.
- **Survival Intel** — Safe zones, evacuation routes, hazard avoidance.
- **Resource Intel** — Hidden deposits, advanced extraction, lost trade routes.
- **Sacred Geometry / Ancient Knowledge** — Blueprints and technology lost to history.
- **Society Ranks** — Passwords, oaths, and rank insignia of pre-catastrophe factions.

Secrets range from Common (earned via daily tasks) to Legendary (earned through full catastrophe cycles). They unlock new mechanics, story content, and strategic advantages — **but are always earnable through gameplay**. See [`docs/secrets.md`](docs/secrets.md) for the full system design.

#### Monetization Guardrails
- Paid secrets = **information, convenience, or cosmetic unlocks** — never raw power
- Every purchasable secret has a **free earn path** through quests, expeditions, or research
- No pay-to-win: a strategic free player out-prepares a careless paying player
- Billing is abstracted behind `BillingProvider` — stub for offline, swappable for Play Billing or Stripe

### ⚠️ Catastrophe Cycle — The Clock You Can't Read
History doesn't repeat — it **reloads**. You know another catastrophe is coming. You don't know when. A **Forecast Meter** gives you signals, but the timing is uncertain by design.

This creates genuine strategic tension:
- **Build now** or **prepare for later**?
- **Hoard resources** or **invest in knowledge**?
- **Keep secrets** or **share them** with your community?

See [`docs/catastrophe-cycle.md`](docs/catastrophe-cycle.md) for the full cycle design.

---

## Project Structure

```
/
├── index.html              # Interactive web prototype (Secrets Library + core UI)
├── data/
│   └── secrets.json        # Data-driven secrets catalogue (10 MVP secrets)
├── src/
│   └── secrets/
│       ├── SecretsData.kt       # Kotlin data classes for Android/Compose
│       ├── SecretsViewModel.kt  # ViewModel: earn/unlock/purchase logic
│       └── SecretsBillingStub.kt# Offline-friendly billing stub
└── docs/
    ├── secrets.md           # Secrets system design
    └── catastrophe-cycle.md # Catastrophe cycle design
```

---

## Running the Prototype

Open `index.html` in any browser. No build step required. The Secrets Library prototype demonstrates:
- Browsing discovered/locked secrets
- Filtering by category and tier
- Unlock animations
- Purchase stub UI
- Catastrophe Forecast Meter

---

## Android / Kotlin (Future)

The `src/secrets/` directory contains Kotlin data classes and ViewModel stubs ready to drop into an Android/Compose project. The architecture follows:

- **Data layer**: `SecretsRepository` loads from bundled JSON or remote config
- **Domain layer**: `SecretsViewModel` handles earn/unlock/pacing logic
- **Billing layer**: `BillingProvider` interface with `StubBillingProvider` (offline) — swap for Play Billing when distributing on Google Play

Build target: minSdk 24, targetSdk 34, Kotlin + Compose + Material3.

---

## Monetization Model (Summary)

| Revenue Source | Type | Available |
|---------------|------|-----------|
| Season Pass (Catastrophe Cycle) | Cosmetic + premium narrative | Planned |
| VIP Subscription | Convenience + cosmetics | Planned |
| IAP Packs (Starter, Convenience, Cosmetic) | One-time / repeatable | Stub ready |
| Rewarded Ads (optional) | Player-chosen | Planned |
| Remove Ads (one-time) | Removes ad prompts | Planned |

All monetization is **optional** and **non-pay-to-win** by design.

---

## Design Philosophy

1. **Offline-first**: fully playable without a network connection
2. **Optional sync**: account/backup is a stub — disabled until you choose a provider
3. **No pay-to-win**: secrets bought = secrets earnable; only timing differs
4. **Lean codebase**: minimal dependencies, data-driven design, stubs for everything unbuilt
5. **Suspense through uncertainty**: the catastrophe clock never shows an exact time
