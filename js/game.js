/**
 * CIVILTAS — Core Game State
 * ===========================
 * Minimal game-loop state that integrates with the Secrets system.
 * Offline-first: all state persisted to localStorage.
 *
 * Integration points for Secrets:
 *   - SecretStore.unlock() is called when earn conditions are met
 *   - getActiveEffects() aggregates all effects from unlocked Secrets
 *   - The forecast panel reads getForecastStatus() from forecast.js
 *
 * This is the MVP scaffold. Expand each section as the game develops.
 */

'use strict';

import { SecretStore }       from './secrets/store.js';
import { getAllSecrets }      from './secrets/catalog.js';
import { getForecastStatus } from './secrets/forecast.js';

const GAME_STORAGE_KEY = 'civiltas_game_state';

// ---------------------------------------------------------------------------
// Default state
// ---------------------------------------------------------------------------
const DEFAULT_STATE = {
  version:     1,
  resources: {
    stone:   0,
    wood:    0,
    ore:     0,
  },
  gnosisXp:    0,
  gnosisRank:  0,   // 0–5 (see docs/secrets-system.md §5)
  milestones:  [],  // string[] of completed milestone IDs
  expeditions: [],  // { id, returnAt } completed expedition records
  lastTick:    Date.now(),
};

// Gnosis XP thresholds for each rank
const GNOSIS_RANK_THRESHOLDS = [0, 100, 300, 700, 1500, 3000];

// ---------------------------------------------------------------------------
// State management
// ---------------------------------------------------------------------------
let _state = _loadState();

function _loadState() {
  try {
    const raw = localStorage.getItem(GAME_STORAGE_KEY);
    if (!raw) return { ...DEFAULT_STATE };
    const saved = JSON.parse(raw);
    // Merge with defaults to handle new fields added in updates
    return { ...DEFAULT_STATE, ...saved };
  } catch {
    return { ...DEFAULT_STATE };
  }
}

function _saveState() {
  localStorage.setItem(GAME_STORAGE_KEY, JSON.stringify(_state));
}

/**
 * Returns the current game state (read-only snapshot).
 * @returns {typeof DEFAULT_STATE}
 */
function getState() {
  return { ..._state };
}

// ---------------------------------------------------------------------------
// Gnosis system
// ---------------------------------------------------------------------------

/**
 * Award Gnosis XP and check for rank-ups.
 * @param {number} amount
 */
function awardGnosisXp(amount) {
  // Apply XP rate bonus from unlocked Secrets
  const bonusRate = getActiveEffects()
    .filter(e => e.type === 'GNOSIS_XP_RATE')
    .reduce((sum, e) => sum + e.value, 0);

  const effective = amount * (1 + bonusRate);
  _state.gnosisXp += effective;

  // Check rank-up
  const nextRank = _state.gnosisRank + 1;
  if (nextRank < GNOSIS_RANK_THRESHOLDS.length &&
      _state.gnosisXp >= GNOSIS_RANK_THRESHOLDS[nextRank]) {
    _state.gnosisRank = nextRank;
    console.info(`[Game] Gnosis rank up: ${_state.gnosisRank}`);
    window.dispatchEvent(new CustomEvent('gnosisRankUp', { detail: { rank: _state.gnosisRank } }));
  }

  _saveState();
  _checkUnlockConditions();
}

// ---------------------------------------------------------------------------
// Resource system
// ---------------------------------------------------------------------------

/**
 * Add resources, applying any active mining yield bonuses.
 * @param {'stone'|'wood'|'ore'} resource
 * @param {number} baseAmount
 */
function mineResource(resource, baseAmount) {
  const bonusRate = getActiveEffects()
    .filter(e => e.type === 'MINING_YIELD' && (e.target === resource || e.target === 'all'))
    .reduce((sum, e) => sum + e.value, 0);

  const effective = baseAmount * (1 + bonusRate);
  _state.resources[resource] = (_state.resources[resource]) + effective;
  _saveState();
}

// ---------------------------------------------------------------------------
// Milestone & Expedition tracking
// ---------------------------------------------------------------------------

/**
 * Record a completed milestone and check if any Secrets become earnable.
 * @param {string} milestoneId
 */
function completeMilestone(milestoneId) {
  if (_state.milestones.includes(milestoneId)) return;
  _state.milestones.push(milestoneId);
  _saveState();
  _checkUnlockConditions();
}

/**
 * Record a completed expedition and check unlock conditions.
 * @param {string} expeditionId
 */
function completeExpedition(expeditionId) {
  if (!_state.expeditions.find(e => e.id === expeditionId)) {
    _state.expeditions.push({ id: expeditionId, returnAt: Date.now() });
    _saveState();
    _checkUnlockConditions();
  }
}

// ---------------------------------------------------------------------------
// Secrets unlock condition evaluator
// ---------------------------------------------------------------------------

/**
 * Evaluates all unlock conditions for all locked Secrets and unlocks any
 * whose conditions are now satisfied.
 * Call this after any state change that could trigger a condition.
 */
function _checkUnlockConditions() {
  for (const secret of getAllSecrets()) {
    if (SecretStore.isUnlocked(secret.id)) continue;
    if (_allConditionsMet(secret.unlockConditions)) {
      SecretStore.unlock(secret.id);
      console.info(`[Game] Secret earned: "${secret.id}"`);
    }
  }
}

/**
 * @param {import('./secrets/model.js').Condition[]} conditions
 * @returns {boolean}
 */
function _allConditionsMet(conditions) {
  return conditions.every(condition => _conditionMet(condition));
}

/**
 * @param {import('./secrets/model.js').Condition} condition
 * @returns {boolean}
 */
function _conditionMet(condition) {
  switch (condition.type) {
    case 'GNOSIS_LEVEL':
      return _state.gnosisRank >= condition.value;
    case 'MILESTONE':
      return _state.milestones.includes(condition.target);
    case 'QUEST_COMPLETE':
      return _state.milestones.includes(`quest_${condition.target}`);
    case 'EXPEDITION_RETURN':
      return _state.expeditions.some(e => e.id === condition.target);
    case 'FRAGMENTS_COMBINED':
      // Fragment system is a future expansion; always false for now
      return false;
    default:
      return false;
  }
}

// ---------------------------------------------------------------------------
// Active effects aggregator
// ---------------------------------------------------------------------------

/**
 * Returns a flat list of all Effect objects from currently unlocked Secrets.
 * Game systems query this to apply bonuses.
 * @returns {import('./secrets/model.js').Effect[]}
 */
function getActiveEffects() {
  return getAllSecrets()
    .filter(s => SecretStore.isUnlocked(s.id))
    .flatMap(s => s.effects);
}

// ---------------------------------------------------------------------------
// Catastrophe event handler (stub)
// ---------------------------------------------------------------------------

/**
 * Applies catastrophe damage to resources, reduced by RISK_REDUCTION effects.
 * @param {number} baseLossRate - fraction of resources lost (0–1)
 */
function applyCatastropheEvent(baseLossRate) {
  const reductionRate = getActiveEffects()
    .filter(e => e.type === 'RISK_REDUCTION' && e.target === 'evacuation')
    .reduce((sum, e) => sum + e.value, 0);

  const effectiveLoss = Math.max(0, baseLossRate - reductionRate);

  for (const resource of Object.keys(_state.resources)) {
    _state.resources[resource] = Math.floor(
      _state.resources[resource] * (1 - effectiveLoss)
    );
  }

  _saveState();
  console.info(`[Game] Catastrophe applied. Loss rate: ${Math.round(effectiveLoss * 100)}%`);
  window.dispatchEvent(new CustomEvent('catastropheApplied', {
    detail: { baseLossRate, effectiveLoss }
  }));
}

/**
 * Returns current game summary including forecast status.
 */
function getGameSummary() {
  return {
    state:    getState(),
    forecast: getForecastStatus(),
    effects:  getActiveEffects(),
  };
}

// Run unlock check on load (in case state from a previous session now qualifies)
_checkUnlockConditions();

export {
  getState,
  awardGnosisXp,
  mineResource,
  completeMilestone,
  completeExpedition,
  getActiveEffects,
  applyCatastropheEvent,
  getGameSummary,
};
