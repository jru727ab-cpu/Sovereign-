/**
 * CIVILTAS — Secrets System: Data Model
 * ======================================
 * Defines the core data structures for the Secrets system.
 * Pure data — no persistence, no side-effects.
 */

'use strict';

/** @enum {string} */
const SecretCategory = Object.freeze({
  LORE:      'LORE',
  INTEL:     'INTEL',
  BLUEPRINT: 'BLUEPRINT',
  FORECAST:  'FORECAST',
});

/** @enum {string} */
const EffectType = Object.freeze({
  MINING_YIELD:       'MINING_YIELD',       // % bonus to resource extraction rate
  BUILDING_UNLOCK:    'BUILDING_UNLOCK',    // enables a new building type
  FORECAST_BONUS:     'FORECAST_BONUS',     // improves catastrophe forecast confidence
  RISK_REDUCTION:     'RISK_REDUCTION',     // reduces resource loss in catastrophe events
  GNOSIS_XP_RATE:     'GNOSIS_XP_RATE',    // % bonus to Gnosis XP gain
  EXPEDITION_SPEED:   'EXPEDITION_SPEED',   // % bonus to expedition return speed
});

/** @enum {string} */
const ConditionType = Object.freeze({
  GNOSIS_LEVEL:       'GNOSIS_LEVEL',       // player has reached a Gnosis rank
  MILESTONE:          'MILESTONE',          // a named in-game milestone is complete
  QUEST_COMPLETE:     'QUEST_COMPLETE',     // a specific quest is finished
  EXPEDITION_RETURN:  'EXPEDITION_RETURN',  // a specific expedition has returned
  FRAGMENTS_COMBINED: 'FRAGMENTS_COMBINED', // player has combined enough fragments
});

/**
 * A single gameplay effect granted by unlocking a Secret.
 * @typedef {Object} Effect
 * @property {string}     type   - One of EffectType
 * @property {string}     target - Building/resource/system identifier this applies to
 * @property {number}     value  - Magnitude: percentage (e.g. 0.05 = +5%) or flat count
 */

/**
 * A single condition that must be met for a Secret to be earnable for free.
 * All conditions in the array are AND'd together.
 * @typedef {Object} Condition
 * @property {string}          type   - One of ConditionType
 * @property {string}          target - What entity/value to check
 * @property {number|string}   value  - Required threshold or identifier
 */

/**
 * Full definition of a Secret.
 * @typedef {Object} Secret
 * @property {string}     id               - Unique identifier, e.g. "lore_first_fracture"
 * @property {string}     title            - Player-visible display name
 * @property {string}     category         - One of SecretCategory
 * @property {string}     teaser           - Spoiler-free blurb shown while the Secret is locked
 * @property {string}     fullText         - Full content revealed after unlock
 * @property {Effect[]}   effects          - Mechanical changes applied on unlock
 * @property {Condition[]} unlockConditions - Free earn paths (all must be met)
 * @property {string|null} skuId           - Play/store SKU for paid instant-unlock (null = not for sale)
 */

/**
 * Validate that a Secret object has all required fields and valid category.
 * Throws if invalid so catalog errors surface at startup.
 * @param {Secret} secret
 * @returns {Secret} the same object if valid
 */
function validateSecret(secret) {
  const required = ['id', 'title', 'category', 'teaser', 'fullText', 'effects', 'unlockConditions'];
  for (const field of required) {
    if (!(field in secret)) {
      throw new Error(`Secret missing required field "${field}" (id: ${secret.id ?? 'unknown'})`);
    }
  }
  if (!Object.values(SecretCategory).includes(secret.category)) {
    throw new Error(`Secret "${secret.id}" has unknown category "${secret.category}"`);
  }
  return secret;
}

export { SecretCategory, EffectType, ConditionType, validateSecret };
