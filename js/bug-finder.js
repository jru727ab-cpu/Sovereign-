// bug-finder.js — Bug detection UI
const BugFinder = (() => {
  function severityBadge(s) {
    return `<span class="severity-badge severity-${s}">${s}</span>`;
  }

  function renderBugResults(data) {
    const panel = document.getElementById('bugResults');
    if (!data.bugs || !data.bugs.length) {
      panel.innerHTML = `
        <div class="bug-summary">✅ <strong>No bugs detected!</strong></div>
        <p style="color:var(--text-muted)">${escapeHtml(data.summary || '')}</p>`;
      return;
    }

    let html = `<div class="bug-summary">
      Overall severity: ${severityBadge(data.overall_severity)}
      &nbsp; ${data.bugs.length} issue(s) found
    </div>
    <p style="color:var(--text-muted);font-size:.9rem;margin-bottom:.75rem">${escapeHtml(data.summary || '')}</p>`;

    data.bugs.forEach((bug, i) => {
      html += `<div class="bug-item">
        <div class="bug-item-header">
          ${severityBadge(bug.severity)}
          <span style="font-size:.8rem;color:var(--text-muted)">${bug.type || ''}</span>
          ${bug.line ? `<span style="font-size:.8rem;color:var(--text-muted)">Line ${bug.line}</span>` : ''}
        </div>
        <div style="font-size:.92rem">${escapeHtml(bug.description || '')}</div>
        ${bug.fix ? `<div class="bug-fix">${escapeHtml(bug.fix)}</div>` : ''}
      </div>`;
    });

    panel.innerHTML = html;
  }

  async function loadReports() {
    const list = document.getElementById('bugReportsList');
    if (!list) return;
    try {
      const res = await Auth.apiFetch('/api/bugs/reports?limit=10');
      const data = await res.json();
      if (!data.reports || !data.reports.length) {
        list.innerHTML = '<div class="empty-state">No reports yet</div>';
        return;
      }
      list.innerHTML = data.reports.map(r => `
        <div class="report-row" data-id="${r.id}">
          <div>
            <strong>${escapeHtml(r.title || 'Report')}</strong>
            <span style="font-size:.82rem;color:var(--text-muted);margin-left:.5rem">${r.language || ''}</span>
          </div>
          <div style="display:flex;align-items:center;gap:.75rem">
            ${severityBadge(r.severity)}
            <span style="font-size:.82rem;color:var(--text-muted)">${r.bug_count} bug(s)</span>
            <span style="font-size:.78rem;color:var(--text-muted)">${new Date(r.created_at).toLocaleDateString()}</span>
          </div>
        </div>`).join('');
    } catch { list.innerHTML = '<div class="empty-state">Failed to load reports</div>'; }
  }

  function escapeHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  }

  function init() {
    const btn = document.getElementById('analyzeBtn');
    if (!btn) return;

    btn.addEventListener('click', async () => {
      const code = document.getElementById('bugCode').value.trim();
      if (!code) { alert('Please paste some code first'); return; }

      btn.disabled = true; btn.textContent = 'Analyzing... 🔍';
      document.getElementById('bugResults').innerHTML = '<div class="loading">Running analysis...</div>';

      try {
        const res = await Auth.apiFetch('/api/bugs/analyze', {
          method: 'POST',
          body: JSON.stringify({
            code,
            language: document.getElementById('bugLang').value || undefined,
            title: document.getElementById('bugTitle').value || undefined
          })
        });
        const data = await res.json();
        if (!res.ok) {
          document.getElementById('bugResults').innerHTML = `<div class="form-error">${escapeHtml(data.error || 'Analysis failed')}</div>`;
          return;
        }
        renderBugResults(data);
        loadReports();
      } catch {
        document.getElementById('bugResults').innerHTML = '<div class="form-error">Network error, please try again</div>';
      } finally {
        btn.disabled = false; btn.textContent = 'Analyze Code 🔍';
      }
    });

    loadReports();
  }

  return { init, loadReports };
})();
