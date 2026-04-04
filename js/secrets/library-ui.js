/**
 * CIVILTAS — Secrets Library UI
 * ==============================
 * Renders the Secrets Library screen into a given container element.
 *
 * Responsibilities:
 *   - Display all Secrets as locked (silhouette) or unlocked (full) cards
 *   - Show Secret detail panel on card click
 *   - Show purchase CTA (stub) for locked Secrets that have a SKU
 *   - Re-render on 'secretUnlocked' events (fired by SecretStore)
 *
 * Design decisions:
 *   - Pure DOM manipulation; no heavy framework dependency
 *   - All styles are inlined or use CSS classes set in the main stylesheet
 *   - Purchase flow delegated to BillingInterface (stub in MVP)
 */

'use strict';

import { getAllSecrets, getSecretsByCategory } from './catalog.js';
import { SecretStore }                         from './store.js';
import { SecretCategory }                      from './model.js';
import { billingInterface }                    from './billing.js';
import { getForecastStatus, getNextForecastHint } from './forecast.js';

// Category icons (Unicode + label pairs — no external assets needed)
const CATEGORY_ICON = {
  [SecretCategory.LORE]:      { icon: '📜', label: 'Lore' },
  [SecretCategory.INTEL]:     { icon: '🗺️', label: 'Strategy Intel' },
  [SecretCategory.BLUEPRINT]: { icon: '📐', label: 'Blueprint' },
  [SecretCategory.FORECAST]:  { icon: '🔭', label: 'Forecast' },
};

/**
 * Renders a compact Secrets count badge into `container`.
 * Useful for tab headers / nav icons.
 * @param {HTMLElement} container
 */
function renderSecretsBadge(container) {
  const unlocked = SecretStore.getUnlockedIds().length;
  const total    = getAllSecrets().length;
  container.textContent = `${unlocked}/${total}`;
}

/**
 * Render the full Secrets Library into `container`.
 * @param {HTMLElement} container
 */
function renderSecretsLibrary(container) {
  container.innerHTML = '';

  // ── Header ───────────────────────────────────────────────────────────────
  const header = document.createElement('div');
  header.className = 'secrets-header';
  header.innerHTML = `
    <h2 class="secrets-title">🔒 Secrets Library</h2>
    <p class="secrets-subtitle">
      Uncover the truth. Each Secret changes what you know — and what you can do.
    </p>`;
  container.appendChild(header);

  // ── Catastrophe Forecast panel ────────────────────────────────────────────
  container.appendChild(_buildForecastPanel());

  // ── Category sections ─────────────────────────────────────────────────────
  for (const category of Object.values(SecretCategory)) {
    const secrets = getSecretsByCategory(category);
    if (secrets.length === 0) continue;

    const section = document.createElement('div');
    section.className = 'secrets-section';

    const { icon, label } = CATEGORY_ICON[category];
    const heading = document.createElement('h3');
    heading.className = 'secrets-category-heading';
    heading.textContent = `${icon} ${label}`;
    section.appendChild(heading);

    const grid = document.createElement('div');
    grid.className = 'secrets-grid';
    secrets.forEach(secret => grid.appendChild(_buildSecretCard(secret, container)));
    section.appendChild(grid);

    container.appendChild(section);
  }

  // Listen for unlock events to refresh the library automatically
  window.addEventListener('secretUnlocked', () => renderSecretsLibrary(container));
}

// ── Private helpers ──────────────────────────────────────────────────────────

function _buildForecastPanel() {
  const { level, description } = getForecastStatus();
  const { nextLevel, helpfulSecrets } = getNextForecastHint();

  const panel = document.createElement('div');
  panel.className = `secrets-forecast secrets-forecast--${level.toLowerCase()}`;

  panel.innerHTML = `
    <div class="forecast-label">🌀 Catastrophe Forecast</div>
    <div class="forecast-level">${level}</div>
    <div class="forecast-desc">${description}</div>
    ${nextLevel
      ? `<div class="forecast-hint">
           Unlock ${helpfulSecrets.length} more Forecast Secret(s) to reach <strong>${nextLevel}</strong>.
         </div>`
      : '<div class="forecast-hint">Maximum forecast precision reached.</div>'
    }`;

  return panel;
}

/**
 * Build a single Secret card element.
 * @param {import('./model.js').Secret} secret
 * @param {HTMLElement} libraryContainer - parent, used for detail panel injection
 * @returns {HTMLElement}
 */
function _buildSecretCard(secret, libraryContainer) {
  const unlocked = SecretStore.isUnlocked(secret.id);
  const card = document.createElement('div');
  card.className = `secret-card ${unlocked ? 'secret-card--unlocked' : 'secret-card--locked'}`;
  card.setAttribute('role', 'button');
  card.setAttribute('tabindex', '0');
  card.setAttribute('aria-label', unlocked ? secret.title : 'Locked Secret');

  const { icon } = CATEGORY_ICON[secret.category];

  card.innerHTML = `
    <div class="secret-card-icon">${unlocked ? icon : '🔒'}</div>
    <div class="secret-card-title">${unlocked ? secret.title : '???'}</div>
    <div class="secret-card-teaser">${secret.teaser}</div>`;

  card.addEventListener('click', () => _showSecretDetail(secret, unlocked, libraryContainer));
  card.addEventListener('keydown', e => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      _showSecretDetail(secret, unlocked, libraryContainer);
    }
  });

  return card;
}

/**
 * Inject (or replace) a detail panel below the library content.
 * @param {import('./model.js').Secret} secret
 * @param {boolean} unlocked
 * @param {HTMLElement} libraryContainer
 */
function _showSecretDetail(secret, unlocked, libraryContainer) {
  // Remove any existing detail panel
  const existing = libraryContainer.querySelector('.secret-detail-panel');
  if (existing) existing.remove();

  const panel = document.createElement('div');
  panel.className = 'secret-detail-panel';

  const { icon, label } = CATEGORY_ICON[secret.category];

  if (unlocked) {
    // Full content
    panel.innerHTML = `
      <div class="detail-close" role="button" tabindex="0" aria-label="Close">✕</div>
      <div class="detail-category">${icon} ${label}</div>
      <h3 class="detail-title">${secret.title}</h3>
      <div class="detail-body">${secret.fullText.replace(/\n/g, '<br>')}</div>
      ${_renderEffects(secret.effects)}`;
  } else {
    // Locked view: teaser + earn path + optional purchase CTA
    const earnPath = secret.unlockConditions
      .map(c => _conditionLabel(c))
      .join(' and ');

    panel.innerHTML = `
      <div class="detail-close" role="button" tabindex="0" aria-label="Close">✕</div>
      <div class="detail-category">${icon} ${label}</div>
      <h3 class="detail-title">🔒 Locked Secret</h3>
      <div class="detail-teaser">${secret.teaser}</div>
      <div class="detail-earn">
        <strong>How to earn:</strong> ${earnPath}
      </div>
      ${secret.skuId ? _renderPurchaseCTA(secret) : ''}`;
  }

  // Close button behaviour
  panel.querySelector('.detail-close').addEventListener('click', () => panel.remove());
  panel.querySelector('.detail-close').addEventListener('keydown', e => {
    if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); panel.remove(); }
  });

  // Wire up purchase button (if present)
  const buyBtn = panel.querySelector('.detail-buy-btn');
  if (buyBtn) {
    buyBtn.addEventListener('click', () => _handlePurchase(secret, libraryContainer));
  }

  libraryContainer.appendChild(panel);
  panel.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

/**
 * Build the effects summary HTML for a Secret detail panel.
 * @param {import('./model.js').Effect[]} effects
 * @returns {string}
 */
function _renderEffects(effects) {
  if (!effects || effects.length === 0) return '';
  const items = effects.map(e => `<li>${_effectLabel(e)}</li>`).join('');
  return `<div class="detail-effects"><strong>Effects:</strong><ul>${items}</ul></div>`;
}

/**
 * Build the purchase CTA HTML.
 * @param {import('./model.js').Secret} secret
 * @returns {string}
 */
function _renderPurchaseCTA(secret) {
  return `
    <div class="detail-purchase">
      <p class="purchase-note">
        💡 Unlock instantly with a one-time purchase, or earn it free through gameplay.
      </p>
      <button class="btn detail-buy-btn" data-sku="${secret.skuId}">
        Unlock Now (Purchase)
      </button>
    </div>`;
}

/**
 * Handle a purchase CTA click — delegates to BillingInterface (stub).
 * @param {import('./model.js').Secret} secret
 * @param {HTMLElement} libraryContainer
 */
function _handlePurchase(secret, libraryContainer) {
  billingInterface.purchase(
    secret.skuId,
    ({ skuId, transactionId }) => {
      // Receipt validation would happen server-side in production.
      // After validation, unlock the secret:
      SecretStore.unlock(secret.id);
      console.info(`[Billing] Unlocked "${secret.id}" via purchase. tx: ${transactionId}`);
      renderSecretsLibrary(libraryContainer);
    },
    ({ code, message }) => {
      console.warn(`[Billing] Purchase failed for "${secret.skuId}": [${code}] ${message}`);
      _showToast(libraryContainer, `Purchase unavailable: ${message}`, 'error');
    }
  );
}

/**
 * Show a brief toast notification inside the library container.
 * @param {HTMLElement} parent
 * @param {string} message
 * @param {'info'|'error'} type
 */
function _showToast(parent, message, type = 'info') {
  const toast = document.createElement('div');
  toast.className = `secrets-toast secrets-toast--${type}`;
  toast.textContent = message;
  parent.appendChild(toast);
  setTimeout(() => toast.remove(), 4000);
}

// ---------------------------------------------------------------------------
// Label helpers
// ---------------------------------------------------------------------------

function _effectLabel(effect) {
  switch (effect.type) {
    case 'MINING_YIELD':
      return `+${Math.round(effect.value * 100)}% mining yield (${effect.target === 'all' ? 'all resources' : effect.target})`;
    case 'BUILDING_UNLOCK':
      return `Unlocks building: ${effect.target.replace(/_/g, ' ')}`;
    case 'FORECAST_BONUS':
      return `+${effect.value} Catastrophe Forecast confidence step(s)`;
    case 'RISK_REDUCTION':
      return `–${Math.round(effect.value * 100)}% resource loss during ${effect.target} events`;
    case 'GNOSIS_XP_RATE':
      return `+${Math.round(effect.value * 100)}% Gnosis XP gain rate`;
    case 'EXPEDITION_SPEED':
      return `+${Math.round(effect.value * 100)}% expedition speed`;
    default:
      return `${effect.type}: ${effect.target} +${effect.value}`;
  }
}

function _conditionLabel(condition) {
  switch (condition.type) {
    case 'GNOSIS_LEVEL':
      return `Reach Gnosis Rank ${condition.value}`;
    case 'MILESTONE':
      return `Complete: ${condition.target.replace(/_/g, ' ')}`;
    case 'QUEST_COMPLETE':
      return `Finish quest: ${condition.target.replace(/_/g, ' ')}`;
    case 'EXPEDITION_RETURN':
      return `Complete expedition: ${condition.target.replace(/_/g, ' ')}`;
    case 'FRAGMENTS_COMBINED':
      return `Combine ${condition.value} Secret Fragments`;
    default:
      return `${condition.type}: ${condition.target} = ${condition.value}`;
  }
}

export { renderSecretsLibrary, renderSecretsBadge };
