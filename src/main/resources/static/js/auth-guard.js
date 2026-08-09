/**
 * auth-guard.js — RPR Trades
 *
 * Include on EVERY protected page (before page-specific scripts).
 *
 * Security model:
 *  - JWT stored in sessionStorage → each tab is an independent session.
 *  - Multiple users (Importer + Exporter + Admin) can be logged in simultaneously
 *    in separate browser tabs.
 *  - This guard runs synchronously at page load — it redirects immediately
 *    before any dashboard JS executes.
 *
 * What it does:
 *  1. Reads JWT from sessionStorage; redirects to /login if absent.
 *  2. Validates token expiry; redirects to /login if expired.
 *  3. Checks the current page path against the user's role.
 *     If the user is on the wrong role's page, they are redirected to
 *     their own dashboard instead of seeing an "Access Denied" error.
 */

(function () {
    'use strict';

    const token = sessionStorage.getItem('token');
    const role  = sessionStorage.getItem('role');
    const path  = window.location.pathname;

    // ── 1. No token → login ───────────────────────────────────────────────────
    if (!token) {
        window.location.replace('/login');
        return;
    }

    // ── 2. Validate token expiry ──────────────────────────────────────────────
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const nowSec  = Math.floor(Date.now() / 1000);
        if (payload.exp && payload.exp < nowSec) {
            sessionStorage.clear();
            window.location.replace('/login');
            return;
        }
    } catch (e) {
        sessionStorage.clear();
        window.location.replace('/login');
        return;
    }

    // ── 3. Role-based page guard ───────────────────────────────────────────────
    const DASHBOARD = {
        ADMIN:    '/dashboard/admin',
        IMPORTER: '/dashboard/importer',
        EXPORTER: '/dashboard/exporter'
    };

    // Paths that belong exclusively to ADMIN
    const ADMIN_PATHS = [
        '/dashboard/admin',
        '/admin/',
        '/admin/users',
        '/admin/reports',
        '/admin/monitoring',
        '/admin/invoices',
        '/admin/documents',
        '/admin/payments',
        '/admin/shipments',
        '/admin/orders',
        '/admin/products',
        '/products/pending'
    ];

    // Paths that belong exclusively to IMPORTER
    const IMPORTER_PATHS = [
        '/dashboard/importer',
        '/orders/my',
        '/payments/pay',
        '/shipments/track',
        '/invoices/my',
        '/documents/my'
    ];

    // Paths that belong exclusively to EXPORTER
    const EXPORTER_PATHS = [
        '/dashboard/exporter',
        '/products/my',
        '/products/add',
        '/orders/exporter',
        '/shipments/create',
        '/shipments/manage',
        '/invoices/manage',
        '/documents/upload'
    ];

    // Paths accessible by both EXPORTER and ADMIN (not importer)
    const EXPORTER_ADMIN_PATHS = [
        '/orders/pending'
    ];

    function startsWithAny(p, list) {
        return list.some(prefix => p === prefix || p.startsWith(prefix + '/') || p.startsWith(prefix + '?'));
    }

    const correctDash = DASHBOARD[role];

    if (!correctDash) {
        // Unknown role — clear and send to login
        sessionStorage.clear();
        window.location.replace('/login');
        return;
    }

    // Admin tries to access importer-only or exporter-only pages
    if (role === 'ADMIN') {
        if (startsWithAny(path, IMPORTER_PATHS) || startsWithAny(path, EXPORTER_PATHS)) {
            window.location.replace(DASHBOARD.ADMIN);
            return;
        }
    }

    // Importer tries to access admin-only, exporter-only, or exporter+admin pages
    if (role === 'IMPORTER') {
        if (
            startsWithAny(path, ADMIN_PATHS) ||
            startsWithAny(path, EXPORTER_PATHS) ||
            startsWithAny(path, EXPORTER_ADMIN_PATHS)
        ) {
            window.location.replace(DASHBOARD.IMPORTER);
            return;
        }
    }

    // Exporter tries to access admin-only or importer-only pages
    if (role === 'EXPORTER') {
        if (startsWithAny(path, ADMIN_PATHS) || startsWithAny(path, IMPORTER_PATHS)) {
            window.location.replace(DASHBOARD.EXPORTER);
            return;
        }
    }

    // ── All checks passed — allow page to load ────────────────────────────────

})();
