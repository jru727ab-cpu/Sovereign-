// storage.js — File storage UI
const StorageManager = (() => {
  function formatBytes(bytes) {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    if (bytes < 1024 * 1024 * 1024) return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
    return `${(bytes / (1024 * 1024 * 1024)).toFixed(2)} GB`;
  }

  function fileIcon(mime) {
    if (!mime) return '📄';
    if (mime.startsWith('image/')) return '🖼️';
    if (mime.startsWith('video/')) return '🎬';
    if (mime.startsWith('audio/')) return '🎵';
    if (mime.includes('pdf')) return '📕';
    if (mime.includes('zip') || mime.includes('tar') || mime.includes('gzip')) return '🗜️';
    if (mime.includes('json') || mime.includes('javascript') || mime.includes('html') || mime.includes('css') || mime.includes('text')) return '📝';
    return '📄';
  }

  async function loadFiles() {
    const grid = document.getElementById('filesList');
    if (!grid) return;
    grid.innerHTML = '<div class="loading">Loading files...</div>';

    try {
      const res = await Auth.apiFetch('/api/storage/files');
      const data = await res.json();

      const used = data.storage_used || 0;
      const limit = data.storage_limit || 1;
      const pct = Math.min(100, Math.round((used / limit) * 100));

      const quotaEl = document.getElementById('storageQuota');
      if (quotaEl) quotaEl.textContent = `${formatBytes(used)} / ${formatBytes(limit)} used (${pct}%)`;

      const fillEl = document.getElementById('quotaFill');
      if (fillEl) fillEl.style.width = `${pct}%`;

      if (!data.files || !data.files.length) {
        grid.innerHTML = '<div class="empty-state">No files uploaded yet. Upload your first file!</div>';
        return;
      }

      grid.innerHTML = data.files.map(f => `
        <div class="file-card">
          <div class="file-icon">${fileIcon(f.mime_type)}</div>
          <div class="file-name" title="${escapeHtml(f.original_name)}">${escapeHtml(f.original_name)}</div>
          <div class="file-meta">${formatBytes(f.file_size)} · ${new Date(f.created_at).toLocaleDateString()}</div>
          <div class="file-actions">
            <a href="${window.API_BASE}/api/storage/files/${f.id}/download?token=${Auth.getToken()}"
               class="btn btn-outline btn-sm" target="_blank">⬇ Download</a>
            <button class="btn btn-ghost btn-sm" data-delete="${f.id}">🗑️</button>
          </div>
        </div>`).join('');

      grid.querySelectorAll('[data-delete]').forEach(btn => {
        btn.addEventListener('click', async () => {
          if (!confirm('Delete this file?')) return;
          try {
            const r = await Auth.apiFetch(`/api/storage/files/${btn.dataset.delete}`, { method: 'DELETE' });
            if (r.ok) loadFiles();
            else alert('Failed to delete file');
          } catch { alert('Network error'); }
        });
      });
    } catch { grid.innerHTML = '<div class="loading">Failed to load files</div>'; }
  }

  function escapeHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  }

  function init() {
    const input = document.getElementById('fileUploadInput');
    if (!input) return;

    input.addEventListener('change', async () => {
      const files = Array.from(input.files);
      if (!files.length) return;

      for (const file of files) {
        const fd = new FormData();
        fd.append('file', file);
        try {
          const res = await fetch(`${window.API_BASE}/api/storage/upload`, {
            method: 'POST',
            headers: { Authorization: `Bearer ${Auth.getToken()}` },
            body: fd
          });
          const data = await res.json();
          if (!res.ok) alert(`Upload failed for ${file.name}: ${data.error}`);
        } catch { alert(`Network error uploading ${file.name}`); }
      }
      input.value = '';
      loadFiles();
    });

    loadFiles();
  }

  return { init, loadFiles };
})();
