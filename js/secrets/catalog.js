/**
 * CIVILTAS — Secrets System: Catalog
 * ====================================
 * Defines all Secrets (earnable + purchasable) and their SKU mappings.
 * Add new Secrets here; the rest of the system picks them up automatically.
 *
 * SKU Naming Convention:
 *   civiltas.<channel>.<type>.<id>
 *   channel  : play | direct | web
 *   type     : secret | bundle | season_pass
 *   id       : kebab-case identifier
 *
 * For the MVP, only "play" channel SKUs are listed. Add "direct" / "web"
 * SKUs when those distribution channels are supported.
 */

'use strict';

import { SecretCategory, EffectType, ConditionType, validateSecret } from './model.js';

// ---------------------------------------------------------------------------
// Initial Secrets (3 launch examples: 1 Lore, 1 Intel, 1 Blueprint)
// ---------------------------------------------------------------------------

/** @type {import('./model.js').Secret[]} */
const SECRETS_CATALOG = [

  // ── LORE ──────────────────────────────────────────────────────────────────
  validateSecret({
    id:       'lore_first_fracture',
    title:    'The First Fracture',
    category: SecretCategory.LORE,
    teaser:   'Someone set this catastrophe in motion deliberately. Their name is closer than you think.',
    fullText: `[PLACEHOLDER — spoiler-safe at launch]

The records recovered from the deep archives are fragmented — deliberately so.
Three signatories appear on a document dated 40 years before the event:
a resource consortium, an unnamed research institute, and a single individual
whose name has been redacted in every surviving copy.

The experiment was meant to be controlled. It wasn't.

What remains is a set of coordinates, a partial formula, and the knowledge
that whoever is responsible left one insurance policy behind — hidden somewhere
in the ruins of the old world.

Effect: You begin to understand the patterns. Gnosis flows more freely (+5% Gnosis XP rate).`,

    effects: [
      { type: EffectType.GNOSIS_XP_RATE, target: 'gnosis', value: 0.05 },
    ],

    unlockConditions: [
      { type: ConditionType.GNOSIS_LEVEL, target: 'gnosis_rank', value: 1 },
    ],

    skuId: 'civiltas.play.secret.lore-first-fracture',
  }),

  // ── INTEL ─────────────────────────────────────────────────────────────────
  validateSecret({
    id:       'intel_safe_corridors',
    title:    'The Safe Corridors',
    category: SecretCategory.INTEL,
    teaser:   'There are paths through the devastation that most will never find. Someone mapped them.',
    fullText: `[PLACEHOLDER — spoiler-safe at launch]

Pre-event survey data, cross-referenced with post-event satellite imagery,
reveals a network of terrain corridors largely untouched by the initial shockwave.
Elevated granite ridgelines, old irrigation valleys repurposed as wind breaks,
and a cluster of reinforced pre-war infrastructure nodes form natural shelters.

The map has three key waypoints:
  ◆ The Northern Ridge (mineral-rich, low flood risk)
  ◆ The Valley of Cisterns (water, modest arable land)
  ◆ The Irongate Plateau (defensible; site of the old research station)

Knowing these routes before the next event means your people don't have to
scramble blind.

Effect: Your community's evacuation protocols improve. Resource loss during
catastrophe events is reduced by 20%.`,

    effects: [
      { type: EffectType.RISK_REDUCTION, target: 'evacuation', value: 0.20 },
    ],

    unlockConditions: [
      { type: ConditionType.EXPEDITION_RETURN, target: 'expedition_scouting_alpha', value: 1 },
    ],

    skuId: 'civiltas.play.secret.intel-safe-corridors',
  }),

  // ── BLUEPRINT ─────────────────────────────────────────────────────────────
  validateSecret({
    id:       'blueprint_foundation_stones',
    title:    'Foundation Stones: Sacred Geometry',
    category: SecretCategory.BLUEPRINT,
    teaser:   'The ancients built to last through chaos. Their method survived when the structures did not.',
    fullText: `[PLACEHOLDER — spoiler-safe at launch]

Recovered from a cache sealed beneath a collapsed university library, this
manuscript describes a pre-modern construction principle sometimes called
"resonance architecture" — the deliberate alignment of load-bearing structures
along naturally stable harmonic lines in the earth.

In practice: foundations laid at specific angular relationships to magnetic north,
combined with a particular ratio of aggregate to binder, create structures that
dissipate rather than absorb shockwave energy.

The builders called these "Foundation Stones." Their workshops were called
Resonance Chambers.

Effect: Unlocks the **Resonance Chamber** building.
  → Reduces structural damage to all adjacent buildings by 15% per catastrophe event.
  → Provides a passive +2% bonus to all mining yields within its radius.`,

    effects: [
      { type: EffectType.BUILDING_UNLOCK, target: 'resonance_chamber', value: 1 },
      { type: EffectType.MINING_YIELD,    target: 'all',               value: 0.02 },
    ],

    unlockConditions: [
      { type: ConditionType.MILESTONE,    target: 'library_built',     value: 1 },
      { type: ConditionType.GNOSIS_LEVEL, target: 'gnosis_rank',       value: 3 },
    ],

    skuId: 'civiltas.play.secret.blueprint-foundation-stones',
  }),

  // ── FORECAST ──────────────────────────────────────────────────────────────
  validateSecret({
    id:       'forecast_precursor_patterns',
    title:    'Precursor Patterns',
    category: SecretCategory.FORECAST,
    teaser:   'The signs were always there. You just didn\'t know how to read them.',
    fullText: `[PLACEHOLDER — spoiler-safe at launch]

A series of environmental anomalies consistently precede a major event:
  1. Micro-seismic clusters along specific fault lines (3–6 weeks before)
  2. Electromagnetic interference with compasses and older electronics (1–2 weeks before)
  3. Unusual migratory behaviour in deep-burrowing fauna (days before)

Cross-referencing these with historical records produces a rough prediction window.
It is imprecise — but it is no longer nothing.

Effect: Catastrophe Forecast confidence upgraded from BLIND → VAGUE.
You will now receive a warning roughly 2 cycles before a major event.`,

    effects: [
      { type: EffectType.FORECAST_BONUS, target: 'catastrophe_forecast', value: 1 },
    ],

    unlockConditions: [
      { type: ConditionType.GNOSIS_LEVEL, target: 'gnosis_rank', value: 2 },
    ],

    skuId: null, // earned only — not sold separately; included in season pass
  }),
];

// ---------------------------------------------------------------------------
// Bundle & Pass SKU definitions (no Secret objects, just purchase metadata)
// ---------------------------------------------------------------------------

/** @type {Array<{skuId: string, label: string, secretIds: string[], priceCents: number}>} */
const BUNDLE_CATALOG = [
  {
    skuId:     'civiltas.play.bundle.genesis-secrets',
    label:     'Genesis Secrets Bundle',
    secretIds: ['lore_first_fracture', 'intel_safe_corridors', 'blueprint_foundation_stones'],
    priceCents: 399,
  },
];

/**
 * @returns {import('./model.js').Secret[]}
 */
function getAllSecrets() {
  return SECRETS_CATALOG;
}

/**
 * @param {string} id
 * @returns {import('./model.js').Secret|undefined}
 */
function getSecretById(id) {
  return SECRETS_CATALOG.find(s => s.id === id);
}

/**
 * @param {string} category - One of SecretCategory
 * @returns {import('./model.js').Secret[]}
 */
function getSecretsByCategory(category) {
  return SECRETS_CATALOG.filter(s => s.category === category);
}

/**
 * @param {string} skuId
 * @returns {import('./model.js').Secret|undefined}
 */
function getSecretBySku(skuId) {
  return SECRETS_CATALOG.find(s => s.skuId === skuId);
}

/**
 * @param {string} skuId
 * @returns {{skuId:string, label:string, secretIds:string[], priceCents:number}|undefined}
 */
function getBundleBySku(skuId) {
  return BUNDLE_CATALOG.find(b => b.skuId === skuId);
}

export {
  getAllSecrets,
  getSecretById,
  getSecretsByCategory,
  getSecretBySku,
  getBundleBySku,
  BUNDLE_CATALOG,
};
