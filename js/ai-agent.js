// ai-agent.js — AI chat interface for the dashboard
const AIAgent = (() => {
  let currentConvId = null;
  let sending = false;

  async function loadConversations() {
    const list = document.getElementById('convList');
    if (!list) return;
    try {
      const res = await Auth.apiFetch('/api/ai/conversations');
      const convs = await res.json();
      list.innerHTML = '';
      if (!convs.length) {
        list.innerHTML = '<div class="loading">No conversations yet</div>';
        return;
      }
      convs.forEach(c => {
        const el = document.createElement('div');
        el.className = `conv-item${c.id === currentConvId ? ' active' : ''}`;
        el.textContent = c.title || 'Conversation';
        el.dataset.id = c.id;
        el.addEventListener('click', () => loadConversation(c.id, c.title));
        list.appendChild(el);
      });
    } catch { list.innerHTML = '<div class="loading">Failed to load</div>'; }
  }

  async function loadConversation(id, title) {
    currentConvId = id;
    document.getElementById('chatTitle').textContent = title || 'Conversation';
    document.querySelectorAll('.conv-item').forEach(el => {
      el.classList.toggle('active', el.dataset.id === id);
    });

    const msgs = document.getElementById('chatMessages');
    msgs.innerHTML = '<div class="loading">Loading...</div>';
    try {
      const res = await Auth.apiFetch(`/api/ai/conversations/${id}/messages`);
      const messages = await res.json();
      msgs.innerHTML = '';
      if (!messages.length) {
        msgs.innerHTML = '<div class="chat-empty">No messages yet</div>';
        return;
      }
      messages.forEach(m => appendMessage(m.role, m.content, m.created_at));
      msgs.scrollTop = msgs.scrollHeight;
    } catch { msgs.innerHTML = '<div class="loading">Failed to load messages</div>'; }
  }

  function appendMessage(role, content, time) {
    const msgs = document.getElementById('chatMessages');
    const empty = msgs.querySelector('.chat-empty, .loading');
    if (empty) empty.remove();

    const el = document.createElement('div');
    el.className = `chat-msg ${role}`;
    const timeStr = time ? new Date(time).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '';
    el.innerHTML = `
      <div class="chat-msg-bubble">${escapeHtml(content)}</div>
      <div class="chat-msg-time">${timeStr}</div>`;
    msgs.appendChild(el);
    msgs.scrollTop = msgs.scrollHeight;
  }

  async function sendMessage() {
    if (sending) return;
    const input = document.getElementById('chatInput');
    const msg = input.value.trim();
    if (!msg) return;

    sending = true;
    const sendBtn = document.getElementById('sendMsgBtn');
    sendBtn.disabled = true;
    input.value = '';

    appendMessage('user', msg, new Date().toISOString());

    // Typing indicator
    const msgs = document.getElementById('chatMessages');
    const typing = document.createElement('div');
    typing.className = 'typing-indicator';
    typing.textContent = 'AI is thinking...';
    msgs.appendChild(typing);
    msgs.scrollTop = msgs.scrollHeight;

    try {
      const res = await Auth.apiFetch('/api/ai/chat', {
        method: 'POST',
        body: JSON.stringify({ message: msg, conversation_id: currentConvId })
      });
      const data = await res.json();
      typing.remove();

      if (!res.ok) {
        appendMessage('assistant', `Error: ${data.error || 'Unknown error'}`, new Date().toISOString());
        return;
      }

      if (!currentConvId) {
        currentConvId = data.conversation_id;
        loadConversations();
      }
      appendMessage('assistant', data.reply, new Date().toISOString());
      document.getElementById('chatTitle').textContent = currentConvId ? document.getElementById('chatTitle').textContent : msg.slice(0, 40);
    } catch (err) {
      typing.remove();
      appendMessage('assistant', 'Network error. Please try again.', new Date().toISOString());
    } finally {
      sending = false;
      sendBtn.disabled = false;
      input.focus();
    }
  }

  function escapeHtml(str) {
    return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
  }

  function init() {
    const newBtn = document.getElementById('newConvBtn');
    const sendBtn = document.getElementById('sendMsgBtn');
    const input = document.getElementById('chatInput');
    if (!newBtn) return;

    newBtn.addEventListener('click', () => {
      currentConvId = null;
      document.getElementById('chatMessages').innerHTML = '<div class="chat-empty">Start a new conversation</div>';
      document.getElementById('chatTitle').textContent = 'New Conversation';
      document.querySelectorAll('.conv-item').forEach(el => el.classList.remove('active'));
    });

    sendBtn.addEventListener('click', sendMessage);
    input.addEventListener('keydown', e => {
      if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); sendMessage(); }
    });

    loadConversations();
  }

  return { init, loadConversations };
})();
