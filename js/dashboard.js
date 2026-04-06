// dashboard.js — Main dashboard controller
(function initDashboard() {
  // Redirect to login if not authenticated
  if (!Auth.isLoggedIn()) {
    window.location.href = '/';
    return;
  }

  const user = Auth.getUser();
  if (user) {
    const el = document.getElementById('sidebarUsername');
    if (el) el.textContent = user.username || user.email;
    const av = document.getElementById('sidebarAvatar');
    if (av) av.textContent = (user.username || user.email || '?')[0].toUpperCase();
  }

  // Logout
  document.getElementById('logoutBtn')?.addEventListener('click', Auth.logout.bind(Auth));

  // Mobile sidebar toggle
  const sidebar = document.getElementById('sidebar');
  document.getElementById('sidebarToggle')?.addEventListener('click', () => {
    sidebar?.classList.toggle('open');
  });

  // View switching
  const views = document.querySelectorAll('.view');
  const navItems = document.querySelectorAll('.nav-item[data-view]');
  let activeView = 'home';

  function showView(name) {
    activeView = name;
    views.forEach(v => v.classList.toggle('active', v.id === `view-${name}`));
    navItems.forEach(n => n.classList.toggle('active', n.dataset.view === name));
    sidebar?.classList.remove('open');

    // Lazy-init each view on first visit
    switch (name) {
      case 'home':     loadHomeStats(); break;
      case 'ai':       AIAgent.init(); break;
      case 'bugs':     BugFinder.init(); break;
      case 'storage':  StorageManager.init(); break;
      case 'xp':       XPSystem.init(); break;
      case 'billing':  Payments.init(); break;
      case 'settings': initSettings(); break;
    }
  }

  navItems.forEach(n => n.addEventListener('click', e => {
    e.preventDefault();
    showView(n.dataset.view);
  }));

  // ===== HOME STATS =====
  async function loadHomeStats() {
    try {
      const res = await Auth.apiFetch('/api/dashboard/stats');
      const data = await res.json();

      setText('statXP',    (data.xp?.total_xp || 0).toLocaleString());
      setText('statLevel',  data.xp?.level || 1);
      setText('statAI',    (data.ai?.messages || 0).toLocaleString());
      setText('statBugs',  data.bugs?.total || 0);
      setText('statFiles', data.files?.count || 0);
      setText('statAch',   data.achievements?.count || 0);

      const pct = data.xp?.progress_pct ?? 0;
      setText('xpLevel',    data.xp?.level || 1);
      setText('xpProgress', pct);
      const fill = document.getElementById('xpBarFill');
      if (fill) fill.style.width = `${pct}%`;

      // Update sidebar username if not set
      if (data.user?.username) {
        const el = document.getElementById('sidebarUsername');
        if (el) el.textContent = data.user.username;
        const av = document.getElementById('sidebarAvatar');
        if (av) av.textContent = data.user.username[0].toUpperCase();
      }
    } catch { /* silent */ }

    loadActivity();
  }

  async function loadActivity() {
    const feed = document.getElementById('activityFeed');
    if (!feed) return;
    try {
      const res = await Auth.apiFetch('/api/dashboard/activity');
      const items = await res.json();
      if (!items.length) { feed.innerHTML = '<div class="empty-state">No activity yet — start using the platform!</div>'; return; }

      feed.innerHTML = items.map(item => {
        const icon = item.type === 'achievement' ? '🏅' : (item.value > 0 ? '⭐' : '📋');
        const valueStr = item.type === 'xp' ? `+${item.value} XP` : (item.value ? `+${item.value} XP` : '');
        return `<div class="activity-item">
          <div class="activity-icon">${icon}</div>
          <div class="activity-desc">
            <strong>${escapeHtml(item.title || item.action || 'Action')}</strong>
            ${item.description ? `<br><span style="color:var(--text-muted);font-size:.85rem">${escapeHtml(item.description)}</span>` : ''}
          </div>
          <div style="text-align:right">
            ${valueStr ? `<div class="activity-value">${valueStr}</div>` : ''}
            <div class="activity-time">${new Date(item.created_at).toLocaleString()}</div>
          </div>
        </div>`;
      }).join('');
    } catch { feed.innerHTML = '<div class="empty-state">Failed to load activity</div>'; }
  }

  // ===== SETTINGS =====
  async function initSettings() {
    try {
      const res = await Auth.apiFetch('/api/auth/me');
      const user = await res.json();
      const usernameEl = document.getElementById('settingsUsername');
      const avatarEl   = document.getElementById('settingsAvatar');
      if (usernameEl) usernameEl.value = user.username || '';
      if (avatarEl)   avatarEl.value   = user.avatar_url || '';
    } catch { /* silent */ }

    document.getElementById('saveProfileBtn')?.addEventListener('click', async () => {
      const err = document.getElementById('settingsError');
      const ok  = document.getElementById('settingsSuccess');
      err.classList.add('hidden'); ok.classList.add('hidden');

      try {
        const res = await Auth.apiFetch('/api/auth/profile', {
          method: 'PUT',
          body: JSON.stringify({
            username: document.getElementById('settingsUsername')?.value || undefined,
            avatar_url: document.getElementById('settingsAvatar')?.value || undefined
          })
        });
        const data = await res.json();
        if (!res.ok) { err.textContent = data.error || 'Failed'; err.classList.remove('hidden'); return; }
        ok.textContent = 'Profile saved!'; ok.classList.remove('hidden');
        const el = document.getElementById('sidebarUsername');
        if (el && data.username) el.textContent = data.username;
      } catch {
        err.textContent = 'Network error'; err.classList.remove('hidden');
      }
    });
  }

  function setText(id, val) {
    const el = document.getElementById(id);
    if (el) el.textContent = val;
  }

  function escapeHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  }

  // Start on home view
  showView('home');
})();
