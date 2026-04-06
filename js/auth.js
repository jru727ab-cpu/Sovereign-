// auth.js — JWT auth helpers shared across pages
const Auth = (() => {
  const TOKEN_KEY = 'aw_token';
  const USER_KEY  = 'aw_user';

  function getToken() { return localStorage.getItem(TOKEN_KEY); }
  function getUser()  {
    try { return JSON.parse(localStorage.getItem(USER_KEY) || 'null'); }
    catch { return null; }
  }
  function isLoggedIn() { return !!getToken(); }

  function setSession(token, user) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  function clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }

  async function apiFetch(path, opts = {}) {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json', ...(opts.headers || {}) };
    if (token) headers['Authorization'] = `Bearer ${token}`;
    const res = await fetch(`${window.API_BASE}${path}`, { ...opts, headers });
    if (res.status === 401) {
      clearSession();
      window.location.href = '/';
    }
    return res;
  }

  async function login(email, password) {
    const res = await fetch(`${window.API_BASE}/api/auth/login`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Login failed');
    setSession(data.token, data.user);
    return data;
  }

  async function signup(email, password, username) {
    const res = await fetch(`${window.API_BASE}/api/auth/signup`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password, username })
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Signup failed');
    setSession(data.token, data.user);
    return data;
  }

  function logout() {
    apiFetch('/api/auth/logout', { method: 'POST' }).catch(() => {});
    clearSession();
    window.location.href = '/';
  }

  return { getToken, getUser, isLoggedIn, apiFetch, login, signup, logout };
})();

// ===== Landing page auth modal wiring =====
(function initLandingAuth() {
  const modal  = document.getElementById('authModal');
  if (!modal) return; // not on landing page

  // If already logged in, go to dashboard
  if (Auth.isLoggedIn()) {
    window.location.href = '/dashboard.html';
    return;
  }

  const overlay  = modal;
  const closeBtn = document.getElementById('modalClose');
  const tabs     = document.querySelectorAll('.tab-btn');
  const loginForm  = document.getElementById('loginForm');
  const signupForm = document.getElementById('signupForm');
  const loginErr   = document.getElementById('loginError');
  const signupErr  = document.getElementById('signupError');

  function showModal(tab) {
    overlay.classList.remove('hidden');
    switchTab(tab || 'login');
  }
  function hideModal() { overlay.classList.add('hidden'); }

  function switchTab(tab) {
    tabs.forEach(t => t.classList.toggle('active', t.dataset.tab === tab));
    loginForm.classList.toggle('hidden',  tab !== 'login');
    signupForm.classList.toggle('hidden', tab !== 'signup');
  }

  document.getElementById('loginBtn')?.addEventListener('click', () => showModal('login'));
  document.getElementById('signupBtn')?.addEventListener('click', () => showModal('signup'));
  document.getElementById('heroSignup')?.addEventListener('click', () => showModal('signup'));
  document.getElementById('freePlanBtn')?.addEventListener('click', () => showModal('signup'));
  document.getElementById('proPlanBtn')?.addEventListener('click', () => showModal('signup'));
  document.getElementById('enterprisePlanBtn')?.addEventListener('click', () => showModal('signup'));

  closeBtn.addEventListener('click', hideModal);
  overlay.addEventListener('click', e => { if (e.target === overlay) hideModal(); });
  tabs.forEach(t => t.addEventListener('click', () => switchTab(t.dataset.tab)));

  loginForm.addEventListener('submit', async e => {
    e.preventDefault();
    loginErr.classList.add('hidden');
    const btn = loginForm.querySelector('button[type=submit]');
    btn.disabled = true; btn.textContent = 'Logging in...';
    try {
      await Auth.login(
        document.getElementById('loginEmail').value,
        document.getElementById('loginPassword').value
      );
      window.location.href = '/dashboard.html';
    } catch (err) {
      loginErr.textContent = err.message;
      loginErr.classList.remove('hidden');
    } finally { btn.disabled = false; btn.textContent = 'Log In'; }
  });

  signupForm.addEventListener('submit', async e => {
    e.preventDefault();
    signupErr.classList.add('hidden');
    const btn = signupForm.querySelector('button[type=submit]');
    btn.disabled = true; btn.textContent = 'Creating account...';
    try {
      await Auth.signup(
        document.getElementById('signupEmail').value,
        document.getElementById('signupPassword').value,
        document.getElementById('signupUsername').value
      );
      window.location.href = '/dashboard.html';
    } catch (err) {
      signupErr.textContent = err.message;
      signupErr.classList.remove('hidden');
    } finally { btn.disabled = false; btn.textContent = 'Create Account'; }
  });
})();
