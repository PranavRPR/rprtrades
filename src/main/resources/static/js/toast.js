/**
 * toast.js  — RPR Trades Unified Notification & Confirmation System
 *
 * Usage:
 *   Toast.show("Login Successful", "success")
 *   Toast.show("Access Denied", "danger")
 *   Toast.confirm("Are you sure?", () => deleteItem())
 *
 * Types: success | danger | warning | info
 */

const Toast = (() => {

    // ── Icons ─────────────────────────────────────────────────────────────────
    const ICONS = {
        success: '<i class="bi bi-check-circle-fill"></i>',
        danger:  '<i class="bi bi-x-circle-fill"></i>',
        warning: '<i class="bi bi-exclamation-triangle-fill"></i>',
        info:    '<i class="bi bi-info-circle-fill"></i>'
    };

    const COLORS = {
        success: '#10b981',
        danger:  '#ef4444',
        warning: '#f59e0b',
        info:    '#3b82f6'
    };

    // ── Ensure toast container exists ─────────────────────────────────────────
    function getContainer() {
        let c = document.getElementById('rpr-toast-container');
        if (!c) {
            c = document.createElement('div');
            c.id = 'rpr-toast-container';
            c.style.cssText = [
                'position:fixed',
                'top:20px',
                'right:20px',
                'z-index:99999',
                'display:flex',
                'flex-direction:column',
                'gap:10px',
                'min-width:300px',
                'max-width:380px'
            ].join(';');
            document.body.appendChild(c);
        }
        return c;
    }

    // ── Show toast ────────────────────────────────────────────────────────────
    function show(message, type = 'success', duration = 3500) {
        const container = getContainer();
        const color     = COLORS[type] || COLORS.info;
        const icon      = ICONS[type]  || ICONS.info;

        const toast = document.createElement('div');
        toast.style.cssText = [
            'background:#1e293b',
            'color:#f1f5f9',
            'border-radius:14px',
            'padding:14px 18px',
            'display:flex',
            'align-items:flex-start',
            'gap:12px',
            'box-shadow:0 10px 40px rgba(0,0,0,.4)',
            `border-left:4px solid ${color}`,
            'animation:rprSlideIn .3s ease',
            'cursor:pointer',
            'position:relative',
            'overflow:hidden'
        ].join(';');

        toast.innerHTML = `
            <div style="color:${color};font-size:20px;flex-shrink:0;margin-top:1px">${icon}</div>
            <div style="flex:1;font-size:14px;font-weight:500;line-height:1.5">${message}</div>
            <div style="font-size:18px;color:#64748b;cursor:pointer;flex-shrink:0;margin-top:-1px" onclick="this.closest('[data-rpr-toast]').remove()">×</div>
            <div class="rpr-progress" style="
                position:absolute;bottom:0;left:0;height:3px;
                background:${color};width:100%;
                transition:width ${duration}ms linear
            "></div>`;

        toast.setAttribute('data-rpr-toast', '');
        container.appendChild(toast);

        // Trigger progress bar
        requestAnimationFrame(() => {
            requestAnimationFrame(() => {
                const bar = toast.querySelector('.rpr-progress');
                if (bar) bar.style.width = '0%';
            });
        });

        // Auto-dismiss
        const timer = setTimeout(() => dismiss(toast), duration);

        toast.addEventListener('click', () => {
            clearTimeout(timer);
            dismiss(toast);
        });

        // Inject keyframes once
        if (!document.getElementById('rpr-toast-styles')) {
            const style = document.createElement('style');
            style.id = 'rpr-toast-styles';
            style.textContent = `
                @keyframes rprSlideIn {
                    from { transform: translateX(110%); opacity: 0; }
                    to   { transform: translateX(0);    opacity: 1; }
                }
                @keyframes rprSlideOut {
                    from { transform: translateX(0);    opacity: 1; max-height: 80px; margin-bottom: 0; }
                    to   { transform: translateX(110%); opacity: 0; max-height: 0;    margin-bottom: -10px; }
                }
            `;
            document.head.appendChild(style);
        }
    }

    function dismiss(el) {
        el.style.animation = 'rprSlideOut .3s ease forwards';
        setTimeout(() => el.remove(), 300);
    }

    // ── Confirm modal ─────────────────────────────────────────────────────────
    function confirm(message, onConfirm, onCancel, options = {}) {
        const {
            confirmText  = 'Confirm',
            cancelText   = 'Cancel',
            confirmClass = 'rpr-btn-danger',
            title        = 'Confirm Action'
        } = options;

        // Remove any existing confirm modal
        const existing = document.getElementById('rpr-confirm-overlay');
        if (existing) existing.remove();

        const overlay = document.createElement('div');
        overlay.id = 'rpr-confirm-overlay';
        overlay.style.cssText = [
            'position:fixed',
            'inset:0',
            'background:rgba(0,0,0,.6)',
            'backdrop-filter:blur(4px)',
            'z-index:100000',
            'display:flex',
            'align-items:center',
            'justify-content:center',
            'animation:rprFadeIn .2s ease'
        ].join(';');

        overlay.innerHTML = `
            <div style="
                background:#1e293b;
                border:1px solid #334155;
                border-radius:20px;
                padding:32px;
                max-width:420px;
                width:90%;
                box-shadow:0 25px 60px rgba(0,0,0,.5);
                animation:rprScaleIn .2s ease;
                text-align:center
            ">
                <div style="width:60px;height:60px;background:#ef444420;border-radius:50%;display:flex;align-items:center;justify-content:center;margin:0 auto 18px;font-size:28px;color:#ef4444">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                </div>
                <h5 style="color:#f1f5f9;font-weight:700;margin-bottom:10px">${title}</h5>
                <p style="color:#94a3b8;margin-bottom:24px;line-height:1.6">${message}</p>
                <div style="display:flex;gap:12px;justify-content:center">
                    <button id="rpr-cancel-btn" style="
                        padding:10px 24px;border-radius:12px;border:1px solid #334155;
                        background:transparent;color:#94a3b8;cursor:pointer;font-weight:600;
                        font-size:14px;transition:.2s
                    ">${cancelText}</button>
                    <button id="rpr-confirm-btn" style="
                        padding:10px 24px;border-radius:12px;border:none;
                        background:#ef4444;color:#fff;cursor:pointer;font-weight:600;
                        font-size:14px;transition:.2s
                    ">${confirmText}</button>
                </div>
            </div>`;

        // Extra keyframes for confirm modal
        if (!document.getElementById('rpr-confirm-styles')) {
            const style = document.createElement('style');
            style.id = 'rpr-confirm-styles';
            style.textContent = `
                @keyframes rprFadeIn {
                    from { opacity: 0; }
                    to   { opacity: 1; }
                }
                @keyframes rprScaleIn {
                    from { transform: scale(.9); opacity: 0; }
                    to   { transform: scale(1);  opacity: 1; }
                }
            `;
            document.head.appendChild(style);
        }

        document.body.appendChild(overlay);

        const confirmBtn = document.getElementById('rpr-confirm-btn');
        const cancelBtn  = document.getElementById('rpr-cancel-btn');

        confirmBtn.addEventListener('click', () => {
            overlay.remove();
            if (typeof onConfirm === 'function') onConfirm();
        });

        cancelBtn.addEventListener('click', () => {
            overlay.remove();
            if (typeof onCancel === 'function') onCancel();
        });

        overlay.addEventListener('click', e => {
            if (e.target === overlay) {
                overlay.remove();
                if (typeof onCancel === 'function') onCancel();
            }
        });
    }

    // ── Public API ────────────────────────────────────────────────────────────
    return { show, confirm };

})();

// Global shorthand aliases
function showToast(msg, type = 'success') { Toast.show(msg, type); }
