/**
 * CIVILTAS — Secrets System: Persistence (SecretStore)
 * =====================================================
 * Manages which Secrets are unlocked for the current player.
 * Uses localStorage for offline-first persistence.
 *
 * Production note: Before calling unlock() for a *purchased* Secret, the
 * receipt must be validated server-side (see docs/monetization.md §4).
 * The store itself has no opinion on *how* a Secret was unlocked — it only
 * records that it was.
 */

'use strict';

const STORAGE_KEY = 'civiltas_secrets_unlocked';

/**
 * Returns the raw Set of unlocked secret IDs from localStorage.
 * @returns {Set<string>}
 */
function _loadUnlocked() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return new Set();
    return new Set(JSON.parse(raw));
  } catch {
    return new Set();
  }
}

/**
 * Persists the current Set of unlocked secret IDs to localStorage.
 * @param {Set<string>} unlocked
 */
function _saveUnlocked(unlocked) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify([...unlocked]));
}

// In-memory cache — synchronised with localStorage on each read/write.
let _cache = _loadUnlocked();

const SecretStore = {
  /**
   * Returns true if the given Secret ID has been unlocked.
   * @param {string} secretId
   * @returns {boolean}
   */
  isUnlocked(secretId) {
    return _cache.has(secretId);
  },

  /**
   * Returns an array of all currently unlocked Secret IDs.
   * @returns {string[]}
   */
  getUnlockedIds() {
    return [..._cache];
  },

  /**
   * Marks a Secret as unlocked and persists the change.
   * Emits a 'secretUnlocked' CustomEvent on window (for UI listeners).
   * @param {string} secretId
   * @returns {boolean} true if this was a new unlock, false if already unlocked
   */
  unlock(secretId) {
    if (_cache.has(secretId)) return false;
    _cache.add(secretId);
    _saveUnlocked(_cache);
    window.dispatchEvent(new CustomEvent('secretUnlocked', { detail: { secretId } }));
    return true;
  },

  /**
   * Unlocks multiple secrets at once (e.g., after a bundle purchase).
   * @param {string[]} secretIds
   * @returns {string[]} IDs that were newly unlocked (skipping already-owned)
   */
  unlockMany(secretIds) {
    const newlyUnlocked = secretIds.filter(id => !_cache.has(id));
    newlyUnlocked.forEach(id => _cache.add(id));
    if (newlyUnlocked.length > 0) {
      _saveUnlocked(_cache);
      newlyUnlocked.forEach(id =>
        window.dispatchEvent(new CustomEvent('secretUnlocked', { detail: { secretId: id } }))
      );
    }
    return newlyUnlocked;
  },

  /**
   * Removes a Secret from the unlocked set (for testing/debug only).
   * @param {string} secretId
   */
  _debugLock(secretId) {
    _cache.delete(secretId);
    _saveUnlocked(_cache);
  },

  /**
   * Clears all unlocked Secrets (for testing/debug only).
   */
  _debugReset() {
    _cache = new Set();
    localStorage.removeItem(STORAGE_KEY);
  },

  /**
   * Re-loads from localStorage (call if storage was modified externally).
   */
  reload() {
    _cache = _loadUnlocked();
  },
};

export { SecretStore };
