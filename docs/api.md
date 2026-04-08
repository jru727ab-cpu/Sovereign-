# CIVILTAS — API Reference

This document describes the public Python API exposed by the `game` package.  All modules live under the `game/` directory and are imported by the main entry point `civiltas.py`.

---

## Table of Contents

1. [game.state](#gamestate)
2. [game.resources](#gameresources)
3. [game.buildings](#gamebuildings)
4. [game.population](#gamepopulation)
5. [game.enlightenment](#gameenlightenment)
6. [game.display](#gamedisplay)

---

## game.state

Central data class holding all mutable game state.

### Constants

| Name | Type | Value | Description |
|------|------|-------|-------------|
| `AGES` | `list[str]` | `["Age of Survival", "Age of Society", "Age of Reason", "Age of Gnosis"]` | Ordered list of civilization ages |
| `ENLIGHTENMENT_THRESHOLDS` | `list[int]` | `[0, 50, 150, 300]` | Minimum EP required to enter each age |

### `GameState`

```python
@dataclass
class GameState:
    turn: int = 1
    population: int = 10
    food: float = 30.0
    wood: float = 20.0
    stone: float = 10.0
    gold: float = 0.0
    enlightenment: float = 0.0
    buildings: Dict[str, int] = field(default_factory=dict)
    age_index: int = 0
    gnosis_unlocked: bool = False
```

**Fields**

| Field | Type | Default | Description |
|-------|------|---------|-------------|
| `turn` | `int` | `1` | Current turn number (incremented after each turn) |
| `population` | `int` | `10` | Number of citizens |
| `food` | `float` | `30.0` | Stockpile of food |
| `wood` | `float` | `20.0` | Stockpile of wood |
| `stone` | `float` | `10.0` | Stockpile of stone |
| `gold` | `float` | `0.0` | Stockpile of gold |
| `enlightenment` | `float` | `0.0` | Accumulated Enlightenment Points |
| `buildings` | `Dict[str, int]` | `{}` | Mapping from building name to count |
| `age_index` | `int` | `0` | Index into `AGES`; 0 = Age of Survival |
| `gnosis_unlocked` | `bool` | `False` | `True` once the final age is reached |

**Properties**

| Property | Return type | Description |
|----------|-------------|-------------|
| `age` | `str` | Human-readable name of the current age (`AGES[age_index]`) |

**Methods**

#### `advance_age_if_ready() -> bool`

Checks whether the current `enlightenment` value meets the threshold for any future age and advances `age_index` accordingly.  Also sets `gnosis_unlocked = True` if the final age is reached.

Returns `True` if the civilization advanced to at least one new age; `False` otherwise.

---

## game.resources

Resource harvesting logic.

### Constants

| Name | Type | Value | Description |
|------|------|-------|-------------|
| `BASE_FOOD_PER_WORKER` | `float` | `2.0` | Base food gained per citizen per turn |
| `BASE_WOOD_PER_WORKER` | `float` | `1.5` | Base wood gained per citizen per turn |
| `BASE_STONE_PER_WORKER` | `float` | `1.0` | Base stone gained per citizen per turn |
| `BASE_GOLD_PER_WORKER` | `float` | `0.5` | Base gold gained per citizen per turn |

### Functions

#### `harvest(state: GameState) -> dict`

Computes and applies one turn of resource gathering.  Production is:

```
food  = population × BASE_FOOD_PER_WORKER  + Farm count  × 5.0
wood  = population × BASE_WOOD_PER_WORKER
stone = population × BASE_STONE_PER_WORKER + Mine count  × 3.0
gold  = population × BASE_GOLD_PER_WORKER  + Mine count  × 2.0
```

Mutates `state` in place (adds gained amounts to each resource field).

**Returns** a `dict` with keys `"food"`, `"wood"`, `"stone"`, `"gold"` holding the floating-point amounts gained this turn.

---

## game.buildings

Building definitions and construction.

### Constants

#### `BUILDING_COSTS`

```python
BUILDING_COSTS = {
    "Farm":    (10,  5,  0, 0),   # (wood, stone, gold, food)
    "Mine":    (15, 10,  0, 0),
    "Temple":  (20, 15,  5, 0),
    "Library": (25, 20, 10, 0),
}
```

Each value is a `tuple[int, int, int, int]` of `(wood_cost, stone_cost, gold_cost, food_cost)`.

#### `BUILDING_DESCRIPTIONS`

```python
BUILDING_DESCRIPTIONS = {
    "Farm":    "+5 Food/turn per Farm.",
    "Mine":    "+3 Stone/turn and +2 Gold/turn per Mine.",
    "Temple":  "+2 Enlightenment/turn per Temple.",
    "Library": "+5 Enlightenment/turn per Library.",
}
```

### Functions

#### `can_afford(state: GameState, building: str) -> bool`

Returns `True` if the current resource stockpiles in `state` are sufficient to pay the costs in `BUILDING_COSTS[building]`.  Returns `False` if `building` is not a recognized name.

#### `build(state: GameState, building: str) -> bool`

Attempts to construct a building.  Deducts the required resources from `state` and increments `state.buildings[building]`.

Returns `True` on success, `False` if the player cannot afford it (state is unchanged on failure).

#### `list_buildings() -> list[str]`

Returns a list of all available building names in insertion order: `["Farm", "Mine", "Temple", "Library"]`.

---

## game.population

Population growth and food consumption.

### Constants

| Name | Type | Value | Description |
|------|------|-------|-------------|
| `FOOD_PER_CITIZEN` | `float` | `1.5` | Food consumed per citizen per turn |
| `GROWTH_RATE` | `float` | `0.10` | Fractional population growth when food is available |

### Functions

#### `consume_food(state: GameState) -> float`

Deducts `population × FOOD_PER_CITIZEN` from `state.food` (clamped to a minimum of 0).

Returns the amount of food consumed.

#### `grow_population(state: GameState) -> int`

* If `state.food > 0`: increases `state.population` by `max(1, int(population × GROWTH_RATE))`.
* If `state.food == 0`: decreases `state.population` by `max(1, int(population × 0.05))`, minimum 1 citizen.

Returns the net change in population (positive for growth, negative for starvation).

---

## game.enlightenment

Enlightenment accumulation and age-progression logic.

### Constants

| Name | Type | Value | Description |
|------|------|-------|-------------|
| `ENLIGHTENMENT_PER_TEMPLE` | `float` | `2.0` | EP generated per Temple per turn |
| `ENLIGHTENMENT_PER_LIBRARY` | `float` | `5.0` | EP generated per Library per turn |
| `GNOSIS_TEXT` | `str` | *(multi-line ASCII art)* | Victory message printed when Gnosis is achieved |

### Functions

#### `accumulate_enlightenment(state: GameState) -> float`

Adds `Temple count × ENLIGHTENMENT_PER_TEMPLE + Library count × ENLIGHTENMENT_PER_LIBRARY` to `state.enlightenment`.

Returns the amount of EP gained this turn.

---

## game.display

Text-based terminal UI helpers.

### Functions

#### `print_header(state: GameState) -> None`

Prints a separator line followed by the current turn number and age name.

#### `print_status(state: GameState) -> None`

Prints the current values of population, all resources, enlightenment (with distance to next age threshold), and the list of constructed buildings.

#### `print_build_menu(state: GameState) -> None`

Prints the numbered build menu showing each available building, its costs, its per-turn effect, and whether the player can currently afford it (✓ / ✗).

#### `print_events(events: list[str]) -> None`

Prints each event string (prefixed with `►`) that occurred during the current turn.  Does nothing if `events` is empty.
