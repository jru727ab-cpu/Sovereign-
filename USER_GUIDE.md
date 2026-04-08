# CIVILTAS — User Guide

> *Gain enlightenment, harvest, work for resources, build civilization, gain esoteric hidden Gnosis.*

CIVILTAS is a text-based civilization-building game where you guide a fledgling settlement from humble origins to an enlightened society.  Each turn you gather resources, manage your population, construct buildings, and accumulate Enlightenment Points on the path to Gnosis.

---

## Table of Contents

1. [Getting Started](#getting-started)
2. [Turn Structure](#turn-structure)
3. [Resources](#resources)
4. [Buildings](#buildings)
5. [Population](#population)
6. [Enlightenment & Ages](#enlightenment--ages)
7. [Winning the Game](#winning-the-game)
8. [Strategy Tips](#strategy-tips)

---

## Getting Started

**Requirements:** Python 3.8 or later.

```bash
python civiltas.py
```

The game runs entirely in your terminal.  Each turn the status panel shows your current resources, population, and enlightenment level, followed by the build menu.  Enter a number to construct a building, or `0` to skip building that turn.

---

## Turn Structure

Each turn proceeds in the following order:

1. **Harvest** — Workers and buildings produce Food, Wood, Stone, and Gold.
2. **Enlightenment** — Temples and Libraries generate Enlightenment Points.
3. **Age Check** — If you have reached the next Enlightenment threshold your civilization advances to a new Age.
4. **Food Consumption** — Each citizen consumes 1.5 food.
5. **Population Growth / Starvation** — If food remains, your population grows; if no food remains, it shrinks.
6. **Player Choice** — You choose a building to construct (or skip).

The game ends when you achieve Gnosis (win) or after 100 turns (loss).

---

## Resources

| Resource | Starting Amount | Base Rate per Worker | Notes |
|----------|-----------------|----------------------|-------|
| Food     | 30              | 2.0 / turn           | Required to grow (and sustain) population |
| Wood     | 20              | 1.5 / turn           | Primary construction material |
| Stone    | 10              | 1.0 / turn           | Required for most buildings |
| Gold     | 0               | 0.5 / turn           | Required for advanced buildings |

Resource production scales with population, so growing your people is crucial for long-term output.

---

## Buildings

Buildings are purchased once per turn.  You may only construct one building per turn.  All buildings are permanent and stack (e.g., two Farms produce double the bonus).

| Building | Wood | Stone | Gold | Effect |
|----------|------|-------|------|--------|
| Farm     | 10   | 5     | 0    | +5 Food per turn |
| Mine     | 15   | 10    | 0    | +3 Stone and +2 Gold per turn |
| Temple   | 20   | 15    | 5    | +2 Enlightenment Points per turn |
| Library  | 25   | 20    | 10   | +5 Enlightenment Points per turn |

A checkmark (✓) in the build menu means you can currently afford the building; a cross (✗) means you cannot.

---

## Population

* **Starting population:** 10 citizens.
* Each citizen produces resources every turn (see [Resources](#resources)).
* Each citizen consumes **1.5 food** per turn.
* If food remains after consumption, population grows by **10 %** (minimum +1) each turn.
* If food is exhausted, population shrinks by **5 %** (minimum -1) each turn — be careful not to let your people starve!

---

## Enlightenment & Ages

Enlightenment Points (EP) are produced by Temples and Libraries.  Accumulating enough EP advances your civilization through four Ages:

| Age             | EP Required |
|-----------------|-------------|
| Age of Survival | 0 (start)   |
| Age of Society  | 50          |
| Age of Reason   | 150         |
| Age of Gnosis   | 300         |

Reaching the Age of Gnosis unlocks the hidden Gnosis message and ends the game in victory.

---

## Winning the Game

Accumulate **300 Enlightenment Points** to enter the Age of Gnosis.  The game immediately ends and reveals the esoteric Gnosis text.  You can achieve this in well under 100 turns with an efficient build order.

If 100 turns pass without reaching Gnosis, the game ends and displays your final Enlightenment score and Age.

---

## Strategy Tips

* **Early game** — Prioritize Farms to sustain rapid population growth.  More workers means more resources every turn.
* **Mid game** — Build Mines to unlock Gold income, which you will need for Temples and Libraries.
* **Late game** — Rush Temples and Libraries once you have stable Gold income to accumulate EP as fast as possible.
* **Avoid starvation** — Keep Food production comfortably above `population × 1.5` at all times.  Starvation shrinks your workforce and slows everything down.
* **Stack buildings** — Multiple Libraries are far more EP-efficient than Temples.  One Library generates 5 EP/turn compared to a Temple's 2 EP/turn.
