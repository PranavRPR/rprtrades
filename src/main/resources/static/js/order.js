/**
 * order.js — RPR Trades
 * Handles: create-order form, my-orders page
 * Auth: sessionStorage (tab-isolated)
 */

const API    = '/api';
const token  = sessionStorage.getItem('token');
const userId = sessionStorage.getItem('userId');

const headers = {
    'Authorization': 'Bearer ' + token,
    'Content-Type':  'application/json'
};

function statusBadge(s) {
    if (!s) return '<span class="badge bg-secondary">—</span>';
    const map = {
        PENDING:    'warning text-dark',
        APPROVED:   'success',
        REJECTED:   'danger',
        CANCELLED:  'secondary',
        SHIPPED:    'info text-dark',
        IN_TRANSIT: 'info text-dark',
        DELIVERED:  'success'
    };
    return `<span class="badge bg-${map[s.toUpperCase()] || 'secondary'}">${s.replace(/_/g,' ')}</span>`;
}

// ── Create-order form ─────────────────────────────────────────────────────────
async function loadProductsForOrder() {
    const select = document.getElementById('productId');
    if (!select) return;

    try {
        const r        = await fetch(`${API}/products`, { headers });
        const products = await r.json();
        select.innerHTML = '<option value="">— Select Product —</option>';
        products
            .filter(p => p.status === 'APPROVED')
            .forEach(p => {
                select.innerHTML += `<option value="${p.productId}">${p.productName} — ₹${p.price}</option>`;
            });
    } catch (e) {
        console.error('Products for order form:', e);
    }
}

document.getElementById('orderForm')?.addEventListener('submit', async function (e) {
    e.preventDefault();

    const btn  = this.querySelector('[type=submit]');
    const orig = btn?.innerHTML;
    if (btn) { btn.disabled = true; btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Placing…'; }

    const data = {
        productId:  Number(document.getElementById('productId').value),
        importerId: Number(userId),
        quantity:   Number(document.getElementById('quantity').value)
    };

    try {
        const r = await fetch(`${API}/orders/create`, {
            method: 'POST', headers, body: JSON.stringify(data)
        });

        if (r.ok) {
            Toast.show('Order placed successfully! Waiting for exporter approval.', 'success');
            setTimeout(() => window.location.href = '/orders/my', 1400);
        } else {
            const err = await r.text();
            Toast.show(err || 'Failed to place order.', 'danger');
        }
    } catch (e) {
        Toast.show('Server error. Please try again.', 'danger');
    }

    if (btn) { btn.disabled = false; btn.innerHTML = orig || 'Place Order'; }
});

// ── My orders page ────────────────────────────────────────────────────────────
async function loadMyOrders() {
    const table = document.getElementById('myOrdersTable');
    if (!table) return;

    table.innerHTML = `<tr><td colspan="6" class="text-center py-4">
        <span class="spinner-border spinner-border-sm me-2"></span>Loading orders…
    </td></tr>`;

    try {
        const r      = await fetch(`${API}/orders/my/${userId}`, { headers });
        if (!r.ok) throw new Error(await r.text());
        const orders = await r.json();

        table.innerHTML = '';
        if (!orders.length) {
            table.innerHTML = `<tr><td colspan="6" class="text-center py-4 text-muted">
                No orders yet. <a href="/products">Browse products</a> to get started.
            </td></tr>`;
            return;
        }

        orders.forEach(order => {
            let actionBtn = '<span class="text-muted small">—</span>';

            if (order.status === 'APPROVED') {
                actionBtn = `<a href="/payments/pay" class="btn btn-warning btn-sm fw-semibold">
                    <i class="bi bi-credit-card me-1"></i>Pay Now
                </a>`;
            } else if (order.status === 'SHIPPED' || order.status === 'IN_TRANSIT') {
                actionBtn = `<a href="/shipments/track" class="btn btn-info btn-sm text-white fw-semibold">
                    <i class="bi bi-truck me-1"></i>Track
                </a>`;
            }

            if (order.status === 'PENDING') {
                actionBtn += ` <button class="btn btn-outline-danger btn-sm"
                    onclick="cancelOrder(${order.orderId})">
                    <i class="bi bi-x-circle me-1"></i>Cancel
                </button>`;
            }

            table.innerHTML += `
            <tr>
                <td><strong>#${order.orderId}</strong></td>
                <td>${order.product?.productName || '—'}</td>
                <td>${order.quantity}</td>
                <td class="text-success fw-semibold">₹ ${order.totalAmount}</td>
                <td>${statusBadge(order.status)}</td>
                <td class="text-center">${actionBtn}</td>
            </tr>`;
        });
    } catch (e) {
        table.innerHTML = `<tr><td colspan="6" class="text-center text-danger py-4">${e.message}</td></tr>`;
    }
}

async function cancelOrder(orderId) {
    Toast.confirm(
        `Cancel order #${orderId}? This cannot be undone.`,
        async () => {
            try {
                const r = await fetch(`${API}/orders/cancel/${orderId}`, { method: 'PUT', headers });
                if (r.ok) {
                    Toast.show(`Order #${orderId} cancelled.`, 'warning');
                    loadMyOrders();
                } else {
                    Toast.show(await r.text() || 'Unable to cancel.', 'danger');
                }
            } catch { Toast.show('Server error.', 'danger'); }
        },
        null,
        { confirmText: 'Cancel Order', title: 'Cancel Order' }
    );
}

// ── Auto-init ─────────────────────────────────────────────────────────────────
if (document.getElementById('productId'))    loadProductsForOrder();
if (document.getElementById('myOrdersTable')) loadMyOrders();
