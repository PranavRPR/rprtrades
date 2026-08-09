/**
 * payment.js — RPR Trades
 * Handles: payment form (/payments/pay), payment history
 * Auth: sessionStorage (tab-isolated)
 */

const API    = '/api';
const token  = sessionStorage.getItem('token');
const userId = sessionStorage.getItem('userId');

const headers = {
    'Authorization': 'Bearer ' + token,
    'Content-Type':  'application/json'
};

// ── Load APPROVED orders into payment form select ─────────────────────────────
async function loadOrdersForPayment() {
    const select = document.getElementById('orderId');
    if (!select) return;

    select.innerHTML = '<option value="">Loading approved orders…</option>';

    try {
        const r = await fetch(`${API}/orders/my/${userId}`, { headers });
        if (!r.ok) throw new Error(await r.text());
        const orders = await r.json();

        const approved = orders.filter(o => o.status === 'APPROVED');
        if (!approved.length) {
            select.innerHTML = '<option value="">No approved orders awaiting payment</option>';
            return;
        }

        select.innerHTML = '<option value="">— Select Approved Order —</option>';
        approved.forEach(o => {
            select.innerHTML += `<option value="${o.orderId}">
                Order #${o.orderId} — ${o.product?.productName || '?'} (₹ ${o.totalAmount})
            </option>`;
        });
    } catch (e) {
        select.innerHTML = '<option value="">Could not load orders</option>';
        console.error('loadOrdersForPayment:', e);
    }
}

// ── Payment form submit ───────────────────────────────────────────────────────
document.getElementById('paymentForm')?.addEventListener('submit', async function (e) {
    e.preventDefault();

    const orderIdEl = document.getElementById('orderId');
    const methodEl  = document.getElementById('paymentMethod');
    const btn       = this.querySelector('[type=submit]');
    const orig      = btn?.innerHTML;

    if (!orderIdEl?.value) {
        Toast.show('Please select an order to pay for.', 'warning');
        return;
    }

    if (btn) { btn.disabled = true; btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Processing…'; }

    try {
        const r = await fetch(`${API}/payments/pay`, {
            method:  'POST',
            headers,
            body:    JSON.stringify({
                orderId:       Number(orderIdEl.value),
                paymentMethod: methodEl?.value || 'UPI'
            })
        });

        if (r.ok) {
            Toast.show('Payment submitted! Admin will verify and confirm it shortly.', 'success');
            setTimeout(() => window.location.href = '/orders/my', 1600);
        } else {
            const err = await r.text();
            Toast.show(err || 'Payment failed.', 'danger');
        }
    } catch (e) {
        Toast.show('Server error. Please try again.', 'danger');
    }

    if (btn) { btn.disabled = false; btn.innerHTML = orig || 'Pay Now'; }
});

// ── Payment history table ─────────────────────────────────────────────────────
async function loadPaymentHistory() {
    const table = document.getElementById('paymentHistoryTable');
    if (!table) return;

    table.innerHTML = `<tr><td colspan="5" class="text-center py-3">
        <span class="spinner-border spinner-border-sm me-2"></span>Loading…
    </td></tr>`;

    try {
        const r        = await fetch(`${API}/payments/my/${userId}`, { headers });
        if (!r.ok) throw new Error(await r.text());
        const payments = await r.json();

        table.innerHTML = '';
        if (!payments.length) {
            table.innerHTML = '<tr><td colspan="5" class="text-center py-3 text-muted">No payment history yet.</td></tr>';
            return;
        }

        payments.forEach(p => {
            const statusMap = { PAID: 'success', PENDING: 'warning text-dark', FAILED: 'danger' };
            const cls       = statusMap[p.status] || 'secondary';
            table.innerHTML += `
            <tr>
                <td>#${p.paymentId}</td>
                <td>#${p.order?.orderId || '—'}</td>
                <td class="fw-semibold">₹ ${p.amount}</td>
                <td>${p.paymentMethod || '—'}</td>
                <td><span class="badge bg-${cls}">${p.status}</span></td>
            </tr>`;
        });
    } catch (e) {
        table.innerHTML = `<tr><td colspan="5" class="text-center text-danger py-3">${e.message}</td></tr>`;
    }
}

// ── Auto-init ─────────────────────────────────────────────────────────────────
if (document.getElementById('orderId'))            loadOrdersForPayment();
if (document.getElementById('paymentHistoryTable')) loadPaymentHistory();
