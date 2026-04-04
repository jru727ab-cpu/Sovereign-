/**
 * CIVILTAS — Catastrophe Forecast Mechanic
 * ==========================================
 * Calculates and exposes the player's current forecast confidence level
 * based on which Secrets have been unlocked.
 *
 * Design rationale (see docs/secrets-system.md §6):
 *   FORECAST-category Secrets grant a +1 step improvement to confidence.
 *   Each step narrows the uncertainty window for the next catastrophe event.
 *   This creates genuine strategic value for Secrets beyond pure narrative.
 */

'use strict';

import { SecretStore }   from './store.js';
import { getAllSecrets }  from './catalog.js';
import { SecretCategory, EffectType } from './model.js';

/** @enum {string} */
const ForecastLevel = Object.freeze({
  BLIND:       'BLIND',       // 0 forecast bonuses
  VAGUE:       'VAGUE',       // 1
  APPROXIMATE: 'APPROXIMATE', // 2
  PRECISE:     'PRECISE',     // 3
  PINPOINT:    'PINPOINT',    // 4+
});

/**
 * Human-readable descriptions for each confidence level.
 * @type {Record<string, string>}
 */
const FORECAST_DESCRIPTIONS = {
  BLIND:
    'You sense nothing. The next event could be years away — or tomorrow.',
  VAGUE:
    'Anomalies are increasing. Prepare within the next 2 cycles.',
  APPROXIMATE:
    'Signs point to an event within the next cycle.',
  PRECISE:
    'Imminent. You have roughly 7 days to prepare.',
  PINPOINT:
    'You know the location, scale, and estimated time to the hour. Act now.',
};

const LEVEL_ORDER = [
  ForecastLevel.BLIND,
  ForecastLevel.VAGUE,
  ForecastLevel.APPROXIMATE,
  ForecastLevel.PRECISE,
  ForecastLevel.PINPOINT,
];

/**
 * Computes the current forecast confidence level by summing FORECAST_BONUS
 * effects from all unlocked Secrets.
 *
 * @returns {{ level: string, description: string, bonusPoints: number }}
 */
function getForecastStatus() {
  const allSecrets = getAllSecrets();
  let bonusPoints = 0;

  for (const secret of allSecrets) {
    if (!SecretStore.isUnlocked(secret.id)) continue;
    for (const effect of secret.effects) {
      if (effect.type === EffectType.FORECAST_BONUS) {
        bonusPoints += effect.value;
      }
    }
  }

  const clampedIndex = Math.min(bonusPoints, LEVEL_ORDER.length - 1);
  const level = LEVEL_ORDER[clampedIndex];

  return {
    level,
    description: FORECAST_DESCRIPTIONS[level],
    bonusPoints,
  };
}

/**
 * Returns the next forecast level the player could reach, and which
 * Secrets (locked, with FORECAST_BONUS effects) would get them there.
 * Useful for showing players what to unlock next.
 *
 * @returns {{ nextLevel: string|null, helpfulSecrets: string[] }}
 */
function getNextForecastHint() {
  const { bonusPoints } = getForecastStatus();
  const nextIndex = bonusPoints + 1;

  if (nextIndex >= LEVEL_ORDER.length) {
    return { nextLevel: null, helpfulSecrets: [] };
  }

  const nextLevel = LEVEL_ORDER[nextIndex];
  const helpfulSecrets = getAllSecrets()
    .filter(s =>
      !SecretStore.isUnlocked(s.id) &&
      s.effects.some(e => e.type === EffectType.FORECAST_BONUS)
    )
    .map(s => s.id);

  return { nextLevel, helpfulSecrets };
}

export { ForecastLevel, FORECAST_DESCRIPTIONS, getForecastStatus, getNextForecastHint };
