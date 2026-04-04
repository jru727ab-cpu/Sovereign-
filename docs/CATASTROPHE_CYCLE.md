# CIVILTAS — Catastrophe Cycle Design

## Concept

The **Catastrophe** is CIVILTAS's core tension mechanic. Unlike typical idle games where time is purely positive, CIVILTAS has a looming threat that pressures players to grow their civilization before disaster strikes.

The Catastrophe creates a **pseudo-season** structure:
- Each season lasts **7 real-world days**
- The threat meter fills over the season
- At 100%, the Catastrophe event triggers and the season resets

---

## Threat Meter

The threat meter is a float from 0.0 to 1.0, incremented each game tick:

```
increment = deltaSeconds / SEASON_DURATION_SECONDS
```

The meter fills linearly over 7 days regardless of player actions in Phase 1.

### Threat Labels

| Range | Label | Color |
|-------|-------|-------|
| 0–24% | Calm | 🟢 Green |
| 25–49% | Stirring | 🟡 Yellow |
| 50–74% | Warning | 🟠 Orange |
| 75–89% | Imminent | 🔴 Red |
| 90–100% | CRITICAL | 🟣 Purple |

The color-coded UI creates visceral urgency at high threat levels without being annoying at low threat.

---

## Season Day Tracking

The current season day (0–7) is tracked alongside the progress float. This gives the player a more intuitive understanding of the remaining time ("Season day 3 / 7" is more readable than "43% threat").

---

## Season Reset

When progress reaches 1.0:
- **Phase 1**: Progress and season day reset to 0. No resource penalty.
- **Phase 2 (planned)**: 
  - Resources are taxed (10–30% depending on player preparedness)
  - Temporary debuffs applied to production rates
  - "Survivors" who built sufficient defenses avoid the tax
  - Special story event plays

---

## Player Agency (Phase 2)

In Phase 2, players will be able to **mitigate** catastrophe effects:
- **Fortifications** (new building type): Reduce resource tax %
- **Knowledge threshold**: High knowledge reduces debuff duration
- **Gnosis rank**: Higher ranks unlock catastrophe immunity for one season

---

## Design Intent

The Catastrophe is designed to:

1. **Create urgency** — Players shouldn't feel like idle games are consequence-free waiting
2. **Reward return** — Coming back every day to check the threat level is engaging
3. **Not punish casually** — Phase 1 has no real consequence; the system teaches the mechanic
4. **Scale in Phase 2** — The cycle becomes more meaningful as players have more ways to respond

---

## Season Leaderboard (Phase 2)

Each season will have a leaderboard tracking:
- Total resources accumulated
- Catastrophe survived (Y/N)
- Time-to-first-upgrade milestones

Leaderboards are purely cosmetic and do not affect gameplay balance.

---

## Implementation Notes

The `CatastropheEngine` is a pure object (no dependencies) that operates on `GameState`. It runs every game tick (1 second) and is called from `GameViewModel.tick()`. 

The tick-based approach means the catastrophe advances even if the player is offline — offline progress is computed separately and merged, but the catastrophe timer uses real wall-clock time via `lastSeenEpoch`.
