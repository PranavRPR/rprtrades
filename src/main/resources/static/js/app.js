/**
 * app.js — RPR Trades Shared Utilities
 *
 * Provides:
 *  - Auth helpers (getToken, getRole, authHeaders)
 *  - logout() — clears sessionStorage and redirects to /login
 *  - apiFetch(path, options) — authenticated fetch wrapper with error handling
 *  - statusBadge(s) — shared status badge renderer
 *  - safe(v, fb) — null-safe value helper
 *  - formatDate(d) — date formatting
 *
 * All auth data is stored in sessionStorage (tab-isolated sessions).
 */

// ─── Auth Accessors ──────────────────────────────────────────────────────────
function getToken()  { return sessionStorage.getItem('token'); }
function getUserId() { return sessionStorage.getItem('userId'); }
function getRole()   { return sessionStorage.getItem('role'); }
function getName()   { return sessionStorage.getItem('name') || 'User'; }
function getEmail()  { return sessionStorage.getItem('email'); }

function authHeaders() {
    return {
        'Authorization': 'Bearer ' + getToken(),
        'Content-Type':  'application/json'
    };
}

// ─── Logout ──────────────────────────────────────────────────────────────────
function logout() {
    Toast.confirm(
        'Are you sure you want to log out?',
        () => {
            sessionStorage.clear();
            window.location.replace('/login');
        },
        null,
        { confirmText: 'Logout', cancelText: 'Cancel', title: 'Confirm Logout', confirmClass: 'rpr-btn-danger' }
    );
}

// ─── Authenticated Fetch Wrapper ─────────────────────────────────────────────
/**
 * apiFetch('/api/orders/my/5', { method: 'GET' })
 * Returns parsed JSON on success, throws Error with server message on failure.
 */
async function apiFetch(path, options = {}) {
    const defaults = {
        headers: authHeaders()
    };
    const merged = { ...defaults, ...options, headers: { ...authHeaders(), ...(options.headers || {}) } };
    const res = await fetch(path, merged);
    if (!res.ok) {
        let msg;
        try { msg = await res.text(); } catch { msg = `HTTP ${res.status}`; }
        throw new Error(msg || `HTTP ${res.status}`);
    }
    const ct = res.headers.get('Content-Type') || '';
    if (ct.includes('application/json')) return res.json();
    return res.text();
}

// ─── Status Badge ─────────────────────────────────────────────────────────────
function statusBadge(s) {
    if (!s) return '<span class="badge bg-secondary">—</span>';
    const map = {
        PENDING:    'warning text-dark',
        APPROVED:   'success',
        REJECTED:   'danger',
        CANCELLED:  'secondary',
        SHIPPED:    'info text-dark',
        PAID:       'success',
        FAILED:     'danger',
        CREATED:    'warning text-dark',
        IN_TRANSIT: 'info text-dark',
        DELIVERED:  'success',
        ACTIVE:     'success',
        INACTIVE:   'secondary',
        DISABLED:   'danger'
    };
    const cls = map[s.toUpperCase()] || 'secondary';
    return `<span class="badge bg-${cls}">${s.replace(/_/g, ' ')}</span>`;
}

// ─── Helpers ─────────────────────────────────────────────────────────────────
function safe(v, fb = '—') {
    return (v !== null && v !== undefined && v !== '') ? v : fb;
}

function formatDate(d) {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-IN', {
        year: 'numeric', month: 'short', day: 'numeric'
    });
}

function todayString() {
    return new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
    });
}

// ─── Loading State ────────────────────────────────────────────────────────────
function setLoading(btn, text = 'Loading...') {
    if (!btn) return;
    btn._originalText = btn.innerHTML;
    btn.disabled      = true;
    btn.innerHTML     = `<span class="spinner-border spinner-border-sm me-1"></span>${text}`;
}

function clearLoading(btn, text) {
    if (!btn) return;
    btn.disabled  = false;
    btn.innerHTML = text || btn._originalText || 'Submit';
}

// ─── Sidebar Toggle ───────────────────────────────────────────────────────────
function initSidebar() {
    document.getElementById('menuToggle')?.addEventListener('click', () =>
        document.querySelector('.sidebar')?.classList.toggle('show'));
}
