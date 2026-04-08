/* ================================================================
   Sovereign Command — Game Logic
   Idle/strategy resource management game.
   State is auto-saved to localStorage every 5 seconds.
   ================================================================ */

'use strict';

// ── Constants ────────────────────────────────────────────────────
const SAVE_KEY       = 'sovereign_save_v2';
const TICK_MS        = 200;   // physics tick interval
const SAVE_INTERVAL  = 5000;  // auto-save every 5 s
const FORGE_COOLDOWN = 8;     // seconds
const NODE_MAX       = 1000;

// ── Upgrades catalogue ───────────────────────────────────────────
const UPGRADES = [
  {
    id: 'pickaxe',
    name: '⛏ Iron Pickaxe',
    desc: 'Mine +2 Gold per click.',
    cost: 30,
    currency: 'gold',
    effect: s => { s.mineBonus += 2; },
    purchased: false,
  },
  {
    id: 'forge_efficiency',
    name: '🔥 Forge Efficiency',
    desc: 'Forge Diskon at 1.5× rate.',
    cost: 80,
    currency: 'gold',
    effect: s => { s.forgeRate *= 1.5; },
    purchased: false,
  },
  {
    id: 'node_overclock',
    name: '🌐 Node Overclock',
    desc: 'Each Genesis Node produces +20% more Gold/s.',
    cost: 150,
    currency: 'gold',
    effect: s => { s.nodeBonus *= 1.2; },
    purchased: false,
  },
  {
    id: 'diskon_miner',
    name: '💠 Diskon Harvester',
    desc: 'Passively earn 0.5 Diskon/s.',
    cost: 50,
    currency: 'diskon',
    effect: s => { s.diskonPerSec += 0.5; },
    purchased: false,
  },
  {
    id: 'sovereign_core',
    name: '⚡ Sovereign Core',
    desc: 'Double passive Gold generation.',
    cost: 300,
    currency: 'gold',
    effect: s => { s.nodeBonus *= 2; },
    purchased: false,
  },
];

// ── Default state ────────────────────────────────────────────────
function defaultState() {
  return {
    gold:         0,
    diskon:       0,
    nodes:        1,
    prestige:     0,
    mineBonus:    1,     // gold per manual mine click
    forgeRate:    1.0,   // diskon gained per forge (gold * 0.1 * forgeRate)
    nodeBonus:    1.0,   // multiplier on node passive income
    diskonPerSec: 0,
    forgeCooldown: 0,
    upgrades: {},        // id -> purchased bool
    totalMines:   0,
    totalForges:  0,
    vaultId: generateVaultId(),
  };
}

function generateVaultId() {
  const hex = () => Math.floor(Math.random() * 65536).toString(16).padStart(4, '0').toUpperCase();
  return `SVLT-${hex()}-${hex()}-${hex()}`;
}

// ── State ────────────────────────────────────────────────────────
let state = defaultState();
let lastTick = Date.now();

// ── Save / Load ──────────────────────────────────────────────────
function saveGame() {
  try {
    localStorage.setItem(SAVE_KEY, JSON.stringify(state));
  } catch (_) { /* quota exceeded — ignore */ }
}

function loadGame() {
  try {
    const raw = localStorage.getItem(SAVE_KEY);
    if (!raw) return;
    const saved = JSON.parse(raw);
    // Merge so new fields are always present
    state = Object.assign(defaultState(), saved);
    // Re-apply purchased upgrades' effects
    UPGRADES.forEach(u => {
      if (state.upgrades[u.id]) u.effect(state);
    });
    addLog('💾 Game loaded from save.');
  } catch (_) { /* corrupt save — start fresh */ }
}

// ── Helpers ──────────────────────────────────────────────────────
function fmt(n) {
  if (n >= 1e6) return (n / 1e6).toFixed(2) + 'M';
  if (n >= 1e3) return (n / 1e3).toFixed(2) + 'k';
  return Math.floor(n).toLocaleString();
}

function addLog(msg) {
  const log = document.getElementById('log');
  if (!log) return;
  const p = document.createElement('p');
  p.textContent = msg;
  log.insertBefore(p, log.firstChild);
  while (log.children.length > 40) log.removeChild(log.lastChild);
}

// ── Passive income rates ─────────────────────────────────────────
function goldPerSec() {
  return (state.nodes * 0.05) * state.nodeBonus;
}

function diskonPerSec() {
  return state.diskonPerSec;
}

// ── Actions ──────────────────────────────────────────────────────
function mine() {
  const gained = state.mineBonus;
  state.gold += gained;
  state.totalMines++;
  addLog(`⛏ Mined ${gained} Gold. (Total: ${fmt(state.gold)})`);
  updateUI();
}

function forge() {
  if (state.forgeCooldown > 0) {
    addLog(`⏳ Forge cooling… ${state.forgeCooldown.toFixed(1)}s left.`);
    return;
  }
  if (state.gold < 10) {
    addLog('⚠ Need at least 10 Gold to forge Diskon.');
    return;
  }
  const goldUsed  = Math.min(state.gold, 50);
  const diskonGained = goldUsed * 0.1 * state.forgeRate;
  state.gold       -= goldUsed;
  state.diskon     += diskonGained;
  state.forgeCooldown = FORGE_COOLDOWN;
  state.totalForges++;
  addLog(`🔥 Forged ${diskonGained.toFixed(2)} Diskon from ${fmt(goldUsed)} Gold.`);
  updateUI();
}

function expandNode() {
  if (state.nodes >= NODE_MAX) { addLog('⚠ Maximum Genesis Nodes reached (1000)!'); return; }
  const cost = Math.floor(20 * Math.pow(1.15, state.nodes - 1));
  if (state.gold < cost) { addLog(`⚠ Need ${fmt(cost)} Gold to expand a node.`); return; }
  state.gold -= cost;
  state.nodes++;
  addLog(`🌐 Genesis Node #${state.nodes} online. Network: ${state.nodes}/1000.`);
  updateUI();
  animateNewNode(state.nodes - 1);
}

function convertToGold() {
  if (state.diskon < 5) { addLog('⚠ Need at least 5 Diskon to convert.'); return; }
  const goldGained = state.diskon * 8;
  state.gold  += goldGained;
  state.diskon = 0;
  addLog(`💰 Converted all Diskon → ${fmt(goldGained)} Gold.`);
  updateUI();
}

function prestige() {
  if (state.gold < 500) { addLog('⚠ Need 500 Gold to Prestige.'); return; }
  state.prestige++;
  const savedPrestige = state.prestige;
  const savedVaultId  = state.vaultId;
  // Keep vault id & prestige count across resets
  state = defaultState();
  state.prestige = savedPrestige;
  state.vaultId  = savedVaultId;
  // Prestige bonus: start with more mine power
  state.mineBonus += savedPrestige;
  addLog(`✨ PRESTIGE ${savedPrestige}! Empire reset. You now mine ${state.mineBonus} Gold/click.`);
  renderUpgrades();
  renderNodeGrid();
  updateUI();
}

// ── Node Grid ────────────────────────────────────────────────────
function renderNodeGrid() {
  const grid = document.getElementById('node-grid');
  if (!grid) return;
  grid.innerHTML = '';
  for (let i = 0; i < NODE_MAX; i++) {
    const dot = document.createElement('div');
    dot.className = 'node-dot' + (i < state.nodes ? ' active' : '');
    dot.id = `node-dot-${i}`;
    grid.appendChild(dot);
  }
}

function animateNewNode(index) {
  const dot = document.getElementById(`node-dot-${index}`);
  if (!dot) return;
  dot.classList.add('active', 'pulse');
  setTimeout(() => dot.classList.remove('pulse'), 700);
}

// ── Upgrades ─────────────────────────────────────────────────────
function renderUpgrades() {
  const list = document.getElementById('upgrade-list');
  if (!list) return;
  list.innerHTML = '';

  UPGRADES.forEach(u => {
    const purchased = !!state.upgrades[u.id];
    const item = document.createElement('div');
    item.className = 'upgrade-item';

    const currSymbol = u.currency === 'gold' ? '🪙' : '💠';
    item.innerHTML = `
      <div class="upgrade-info">
        <div class="name">${u.name} ${purchased ? '<span style="color:var(--green);font-size:0.75rem;">✓ Owned</span>' : ''}</div>
        <div class="desc">${u.desc} Cost: ${fmt(u.cost)} ${currSymbol}</div>
      </div>
      <button class="btn btn-outline upgrade-btn" id="upg-btn-${u.id}" ${purchased ? 'disabled' : ''}
        onclick="buyUpgrade('${u.id}')">
        ${purchased ? 'Owned' : 'Buy'}
      </button>
    `;
    list.appendChild(item);
  });
}

function buyUpgrade(id) {
  const u = UPGRADES.find(x => x.id === id);
  if (!u || state.upgrades[u.id]) return;

  const pool = u.currency === 'gold' ? 'gold' : 'diskon';
  if (state[pool] < u.cost) {
    addLog(`⚠ Need ${fmt(u.cost)} ${u.currency} for "${u.name}".`);
    return;
  }

  state[pool] -= u.cost;
  state.upgrades[u.id] = true;
  u.effect(state);
  addLog(`🔧 Upgrade purchased: ${u.name}`);
  renderUpgrades();
  updateUI();
}

// ── UI Update ────────────────────────────────────────────────────
let lastGoldForRate = 0;
let lastDiskonForRate = 0;

function updateUI() {
  // Resources
  document.getElementById('gold-val').textContent    = fmt(state.gold);
  document.getElementById('diskon-val').textContent  = fmt(state.diskon);
  document.getElementById('node-count').textContent  = state.nodes;

  // Rates (displayed, calculated from passive income functions)
  document.getElementById('gold-rate').textContent   = `+${goldPerSec().toFixed(2)}/s`;
  document.getElementById('diskon-rate').textContent = `+${diskonPerSec().toFixed(2)}/s`;

  // Node progress bar
  const pct = (state.nodes / NODE_MAX * 100).toFixed(2);
  document.getElementById('node-progress').style.width = pct + '%';

  // Forge cooldown bar
  const forgePct = state.forgeCooldown <= 0
    ? 100
    : Math.round((1 - state.forgeCooldown / FORGE_COOLDOWN) * 100);
  document.getElementById('forge-cooldown').style.width = forgePct + '%';

  if (state.forgeCooldown > 0) {
    document.getElementById('forge-hint').textContent =
      `Forge cooling… ${state.forgeCooldown.toFixed(1)}s remaining.`;
    document.getElementById('btn-forge').disabled = true;
  } else {
    document.getElementById('forge-hint').textContent =
      state.gold >= 10
        ? `Forge ready! Converts up to 50 Gold → ${(Math.min(state.gold, 50) * 0.1 * state.forgeRate).toFixed(2)} Diskon.`
        : 'Forge ready. Need at least 10 Gold.';
    document.getElementById('btn-forge').disabled = false;
  }

  // Expand node cost hint
  const expandCost = Math.floor(20 * Math.pow(1.15, state.nodes - 1));
  document.getElementById('btn-expand').textContent =
    `🌐 Expand Node (${fmt(expandCost)} 🪙)`;
  document.getElementById('btn-expand').disabled = state.nodes >= NODE_MAX;

  // Prestige button
  document.getElementById('btn-prestige').disabled = state.gold < 500;

  // Prestige counter
  document.getElementById('prestige-count').textContent = state.prestige;

  // Vault address
  document.getElementById('vault-address').textContent = state.vaultId;

  // Status text
  const statusMessages = [
    `Network running: ${state.nodes} node${state.nodes !== 1 ? 's' : ''} active.`,
    `Earning ${goldPerSec().toFixed(2)} Gold/s passively.`,
    `${state.totalMines} mines · ${state.totalForges} forges completed.`,
    `Prestige level ${state.prestige} — empire ever stronger.`,
  ];
  const idx = Math.floor(Date.now() / 4000) % statusMessages.length;
  document.getElementById('status-text').textContent = statusMessages[idx];

  // Update upgrade buttons
  UPGRADES.forEach(u => {
    const btn = document.getElementById(`upg-btn-${u.id}`);
    if (!btn || state.upgrades[u.id]) return;
    const pool = u.currency === 'gold' ? 'gold' : 'diskon';
    btn.disabled = state[pool] < u.cost;
  });

  // Node label
  document.getElementById('node-label').textContent =
    `(${state.nodes}/1000 active — showing all slots)`;
}

// ── Game Tick ────────────────────────────────────────────────────
function tick() {
  const now = Date.now();
  const dt  = (now - lastTick) / 1000; // seconds elapsed
  lastTick  = now;

  // Passive income
  state.gold   += goldPerSec()   * dt;
  state.diskon += diskonPerSec() * dt;

  // Forge cooldown
  if (state.forgeCooldown > 0) {
    state.forgeCooldown = Math.max(0, state.forgeCooldown - dt);
  }

  updateUI();
}

// ── Init ─────────────────────────────────────────────────────────
function init() {
  loadGame();
  renderNodeGrid();
  renderUpgrades();
  updateUI();
  setInterval(tick, TICK_MS);
  setInterval(saveGame, SAVE_INTERVAL);
  addLog('⚡ Sovereign Command v2 online.');
}

document.addEventListener('DOMContentLoaded', init);
