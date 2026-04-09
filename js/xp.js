// xp.js — XP, leaderboard, and achievements UI
const XPSystem = (() => {
  async function loadXP() {
    try {
      const res = await Auth.apiFetch('/api/xp/me');
      const data = await res.json();

      const bigLevel = document.getElementById('xpBigLevel');
      const bigTotal = document.getElementById('xpBigTotal');
      const bigFill  = document.getElementById('xpBigFill');
      const bigNext  = document.getElementById('xpBigNext');

      if (bigLevel) bigLevel.textContent = data.level;
      if (bigTotal) bigTotal.textContent = `${data.total_xp.toLocaleString()} XP`;
      if (bigFill)  bigFill.style.width  = `${data.progress_pct}%`;
      if (bigNext)  bigNext.textContent  = `Next level: ${data.next_level_xp.toLocaleString()} XP (${data.progress_pct}%)`;
    } catch {/* silent */}
  }

  async function loadLeaderboard() {
    const tbody = document.querySelector('#leaderboardTable tbody');
    if (!tbody) return;
    try {
      const res = await Auth.apiFetch('/api/xp/leaderboard');
      const { leaderboard, my_rank } = await res.json();
      const me = Auth.getUser();

      tbody.innerHTML = leaderboard.map(row => `
        <tr class="${row.username === me?.username ? 'me' : ''}">
          <td>${row.rank}</td>
          <td>${escapeHtml(row.username)}</td>
          <td>${row.level}</td>
          <td>${row.total_xp.toLocaleString()}</td>
        </tr>`).join('');
    } catch { tbody.innerHTML = '<tr><td colspan="4">Failed to load</td></tr>'; }
  }

  async function loadAchievements() {
    const grid = document.getElementById('achievementsGrid');
    if (!grid) return;
    try {
      const res = await Auth.apiFetch('/api/xp/achievements');
      const achievements = await res.json();
      grid.innerHTML = achievements.map(a => `
        <div class="achievement-card ${a.earned_at ? 'earned' : 'locked'}">
          <div class="achievement-icon">${a.icon || '🏅'}</div>
          <div class="achievement-name">${escapeHtml(a.name)}</div>
          <div class="achievement-desc">${escapeHtml(a.description || '')}</div>
          <div class="achievement-xp">+${a.xp_reward} XP</div>
          ${a.earned_at ? `<div style="font-size:.75rem;color:var(--success);margin-top:.2rem">✓ Earned ${new Date(a.earned_at).toLocaleDateString()}</div>` : ''}
        </div>`).join('');
    } catch { grid.innerHTML = '<div class="loading">Failed to load achievements</div>'; }
  }

  function escapeHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  }

  function init() {
    loadXP();
    loadLeaderboard();
    loadAchievements();
  }

  return { init, loadXP };
})();
