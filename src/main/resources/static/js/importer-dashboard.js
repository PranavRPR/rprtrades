// ============================================================
// RPR Trades — Importer Dashboard JS
// Uses sessionStorage for tab-isolated auth.
// Cart uses localStorage keyed by userId (shared across tabs for same user).
// ============================================================

const API    = '/api';   // relative — works in any environment
const token  = sessionStorage.getItem('token');
const userId = sessionStorage.getItem('userId');
const name   = sessionStorage.getItem('name') || 'Importer';

const headers = {
    'Authorization': 'Bearer ' + token,
    'Content-Type':  'application/json'
};

// ── Status badge ──────────────────────────────────────────────────────────────
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
        DELIVERED:  'success'
    };
    return `<span class="badge bg-${map[s.toUpperCase()] || 'secondary'}">${s.replace(/_/g, ' ')}</span>`;
}

function safe(v, fb = '—') {
    return (v !== null && v !== undefined && v !== '') ? v : fb;
}

// ── Cart (localStorage, keyed by userId — persists for the same user) ─────────
const CART_KEY = 'rpr_cart_' + (userId || 'guest');

function getCart() {
    try { return JSON.parse(localStorage.getItem(CART_KEY)) || []; }
    catch { return []; }
}

function saveCart(cart) {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    updateCartBadge();
}

function updateCartBadge() {
    const badge = document.getElementById('cartCount');
    if (badge) badge.textContent = getCart().length;
}

function addToCart(productId, productName, price, stock) {
    const cart     = getCart();
    const existing = cart.find(i => i.productId === productId);
    if (existing) {
        if (existing.quantity + 1 > stock) {
            Toast.show('Cannot add more than available stock (' + stock + ').', 'warning');
            return;
        }
        existing.quantity += 1;
    } else {
        cart.push({ productId, productName, price, stock, quantity: 1 });
    }
    saveCart(cart);
    Toast.show(productName + ' added to cart!', 'success');
}

function removeFromCart(productId) {
    saveCart(getCart().filter(i => i.productId !== productId));
    renderCart();
}

function renderCart() {
    const box = document.getElementById('cartItems');
    if (!box) return;
    const cart = getCart();
    box.innerHTML = '';
    if (!cart.length) {
        box.innerHTML = `
        <div class="text-center text-muted py-4">
            <i class="bi bi-cart-x display-4"></i>
            <p class="mt-2 mb-0">Your cart is empty.</p>
            <a href="/products" class="btn btn-primary btn-sm mt-3">
                <i class="bi bi-search me-1"></i>Browse Products
            </a>
        </div>`;
        return;
    }
    let total = 0;
    cart.forEach(item => {
        const lineTotal = item.price * item.quantity;
        total += lineTotal;
        box.innerHTML += `
        <div class="d-flex justify-content-between align-items-center border-bottom py-2 px-1">
            <div>
                <strong>${item.productName}</strong><br>
                <small class="text-muted">₹ ${item.price} × ${item.quantity}</small>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="fw-semibold text-success">₹ ${lineTotal.toFixed(2)}</span>
                <button class="btn btn-outline-danger btn-sm" onclick="removeFromCart(${item.productId})" title="Remove">
                    <i class="bi bi-trash"></i>
                </button>
            </div>
        </div>`;
    });
    box.innerHTML += `
    <div class="d-flex justify-content-between align-items-center mt-3 pt-2">
        <h5 class="mb-0 fw-bold">Total</h5>
        <h5 class="mb-0 text-success fw-bold">₹ ${total.toFixed(2)}</h5>
    </div>
    <button class="btn btn-success w-100 mt-3 fw-semibold" onclick="placeOrderFromCart()">
        <i class="bi bi-bag-check me-1"></i> Place All Orders
    </button>`;
}

async function placeOrderFromCart() {
    const cart = getCart();
    if (!cart.length) { Toast.show('Cart is empty.', 'warning'); return; }

    Toast.confirm(
        `Place ${cart.length} order(s) from your cart?`,
        async () => {
            let placed = 0, failed = 0;
            for (const item of cart) {
                const body = {
                    productId:  item.productId,
                    importerId: Number(userId),
                    quantity:   item.quantity
                };
                try {
                    const r = await fetch(`${API}/orders/create`, {
                        method: 'POST', headers, body: JSON.stringify(body)
                    });
                    if (r.ok) placed++; else failed++;
                } catch { failed++; }
            }
            if (placed > 0) {
                saveCart([]);
                bootstrap.Modal.getInstance(document.getElementById('cartModal'))?.hide();
                Toast.show(`${placed} order(s) placed! Waiting for exporter approval.`, 'success');
                loadOrders();
            }
            if (failed > 0) {
                Toast.show(`${failed} order(s) could not be placed. Check stock or try again.`, 'warning');
            }
        },
        null,
        { confirmText: 'Place Orders', title: 'Confirm Cart Order' }
    );
}

// ── DOMContentLoaded init ─────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    // Greet user
    const nameEl    = document.getElementById('userName');
    const welcomeEl = document.getElementById('welcomeName');
    const avatarEl  = document.getElementById('avatarImg');

    if (nameEl)    nameEl.textContent    = name;
    if (welcomeEl) welcomeEl.textContent = name;
    if (avatarEl)  avatarEl.src =
        'https://ui-avatars.com/api/?background=10b981&color=fff&name=' + encodeURIComponent(name);

    const dateEl = document.getElementById('todayDate');
    if (dateEl) dateEl.textContent = new Date().toLocaleDateString('en-IN', {
        weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
    });

    loadProducts();
    loadOrders();
    loadPayments();
    loadShipments();
    bindSidebar();
    bindActions();
    updateCartBadge();
});

// ── Products ──────────────────────────────────────────────────────────────────
async function loadProducts() {
    try {
        const r        = await fetch(`${API}/products`, { headers });
        if (!r.ok) throw new Error('Failed to load products');
        const products = await r.json();
        const approved = products.filter(p => p.status === 'APPROVED');

        const countEl = document.getElementById('productCount');
        if (countEl) countEl.textContent = approved.length;
        buildProductCards(approved);
    } catch (e) {
        const el = document.getElementById('dashboardProducts');
        if (el) el.innerHTML = `<div class="col-12 text-muted small py-2">Could not load products.</div>`;
    }
}

function buildProductCards(products) {
    const box = document.getElementById('dashboardProducts');
    if (!box) return;
    box.innerHTML = '';
    if (!products.length) {
        box.innerHTML = `<div class="col-12 text-muted py-2">No approved products available yet.</div>`;
        return;
    }
    products.slice(0, 4).forEach(p => {
        const safeName = (p.productName || '').replace(/'/g, "\\'");
        box.innerHTML += `
        <div class="col-lg-3 col-md-6">
            <div class="product-card">
                <img src="https://picsum.photos/400/220?random=${p.productId}" alt="${p.productName}" loading="lazy">
                <div class="product-body">
                    <div class="product-title">${safe(p.productName)}</div>
                    <div class="product-category text-muted small">${safe(p.category)}</div>
                    <div class="product-price">₹ ${safe(p.price)}</div>
                    <small class="text-muted">Stock: ${safe(p.stockQuantity)}</small>
                    <div class="d-flex gap-2 mt-2">
                        <button class="btn btn-outline-primary btn-sm flex-fill"
                            onclick="addToCart(${p.productId},'${safeName}',${p.price},${p.stockQuantity})">
                            <i class="bi bi-cart-plus me-1"></i>Cart
                        </button>
                        <button class="btn btn-success btn-sm flex-fill"
                            onclick="openOrderModal(${p.productId},'${safeName}',${p.price},${p.stockQuantity})">
                            <i class="bi bi-bag-check me-1"></i>Order
                        </button>
                    </div>
                </div>
            </div>
        </div>`;
    });
}

// ── Orders ────────────────────────────────────────────────────────────────────
async function loadOrders() {
    try {
        const r      = await fetch(`${API}/orders/my/${userId}`, { headers });
        if (!r.ok) throw new Error('Failed to load orders');
        const orders = await r.json();

        const countEl = document.getElementById('orderCount');
        const todayEl = document.getElementById('todayOrders');
        if (countEl) countEl.textContent = orders.length;
        if (todayEl) todayEl.textContent = orders.filter(o =>
            new Date(o.createdAt || Date.now()).toDateString() === new Date().toDateString()
        ).length || orders.length;
        buildOrderTable(orders);
    } catch (e) {
        console.warn('Orders load failed:', e.message);
    }
}

function buildOrderTable(data) {
    const tb = document.getElementById('orderTable');
    if (!tb) return;
    tb.innerHTML = '';
    if (!data.length) {
        tb.innerHTML = `<tr><td colspan="7" class="text-center py-4 text-muted">
            No orders yet. <a href="/products">Browse products</a> to place your first order.
        </td></tr>`;
        return;
    }
    data.slice(0, 8).forEach(o => {
        const canPay    = o.status === 'APPROVED';
        const canCancel = o.status === 'PENDING';
        tb.innerHTML += `
        <tr>
            <td><strong>#${o.orderId}</strong></td>
            <td>${safe(o.product?.productName)}</td>
            <td>${safe(o.quantity)}</td>
            <td>${statusBadge(o.status)}</td>
            <td class="text-success fw-semibold">₹ ${safe(o.totalAmount)}</td>
            <td>
                ${canPay
                    ? `<button class="btn btn-warning btn-sm" onclick="openPaymentModal(${o.orderId},${o.totalAmount})">
                           <i class="bi bi-credit-card me-1"></i>Pay
                       </button>`
                    : '<span class="text-muted small">—</span>'}
            </td>
            <td>
                ${canCancel
                    ? `<button class="btn btn-outline-danger btn-sm" onclick="cancelOrder(${o.orderId})">
                           <i class="bi bi-x-circle"></i>
                       </button>`
                    : `<a href="/orders/my" class="btn btn-outline-secondary btn-sm" title="View details">
                           <i class="bi bi-eye"></i>
                       </a>`}
            </td>
        </tr>`;
    });
}

async function cancelOrder(orderId) {
    Toast.confirm(
        `Cancel order #${orderId}? This cannot be undone.`,
        async () => {
            try {
                const r = await fetch(`${API}/orders/cancel/${orderId}`, { method: 'PUT', headers });
                if (r.ok) {
                    Toast.show(`Order #${orderId} cancelled.`, 'warning');
                    loadOrders();
                } else {
                    const msg = await r.text();
                    Toast.show(msg || 'Could not cancel order.', 'danger');
                }
            } catch { Toast.show('Server error.', 'danger'); }
        },
        null,
        { confirmText: 'Cancel Order', title: 'Cancel Order' }
    );
}

// ── Payments ──────────────────────────────────────────────────────────────────
async function loadPayments() {
    try {
        const r        = await fetch(`${API}/payments/my/${userId}`, { headers });
        if (!r.ok) throw new Error('Failed to load payments');
        const payments = await r.json();

        const countEl = document.getElementById('paymentCount');
        const pendEl  = document.getElementById('pendingPayments');
        if (countEl) countEl.textContent = payments.length;
        if (pendEl)  pendEl.textContent  = payments.filter(p => p.status === 'PENDING').length;
        buildPaymentTable(payments);
    } catch (e) {
        console.warn('Payments load failed:', e.message);
    }
}

function buildPaymentTable(data) {
    const tb = document.getElementById('paymentTable');
    if (!tb) return;
    tb.innerHTML = '';
    if (!data.length) {
        tb.innerHTML = `<tr><td colspan="3" class="text-center py-4 text-muted">No payments yet.</td></tr>`;
        return;
    }
    data.slice(0, 6).forEach(p => {
        tb.innerHTML += `
        <tr>
            <td>#${safe(p.order?.orderId)}</td>
            <td class="fw-semibold">₹ ${safe(p.amount)}</td>
            <td>${statusBadge(p.status)}</td>
        </tr>`;
    });
}

// ── Shipments ─────────────────────────────────────────────────────────────────
async function loadShipments() {
    try {
        const r         = await fetch(`${API}/shipments/my/${userId}`, { headers });
        if (!r.ok) throw new Error('Failed to load shipments');
        const shipments = await r.json();

        const countEl  = document.getElementById('shipmentCount');
        const activeEl = document.getElementById('activeShipments');
        if (countEl)  countEl.textContent  = shipments.length;
        if (activeEl) activeEl.textContent = shipments.filter(s =>
            s.status !== 'DELIVERED'
        ).length;
        buildShipmentTable(shipments);
    } catch (e) {
        console.warn('Shipments load failed:', e.message);
    }
}

function buildShipmentTable(data) {
    const tb = document.getElementById('shipmentTable');
    if (!tb) return;
    tb.innerHTML = '';
    if (!data.length) {
        tb.innerHTML = `<tr><td colspan="4" class="text-center py-4 text-muted">No shipments yet.</td></tr>`;
        return;
    }
    data.slice(0, 6).forEach(s => {
        const canReceive = s.status === 'IN_TRANSIT' || s.status === 'CREATED' || s.status === 'SHIPPED';
        tb.innerHTML += `
        <tr>
            <td><code>${safe(s.trackingNumber)}</code></td>
            <td>${safe(s.currentLocation)}</td>
            <td>${statusBadge(s.status)}</td>
            <td>
                ${canReceive
                    ? `<button class="btn btn-success btn-sm" onclick="confirmReceiveDelivery(${s.shipmentId})">
                           <i class="bi bi-box-seam me-1"></i>Received
                       </button>`
                    : `<span class="badge bg-success">Delivered</span>`}
            </td>
        </tr>`;
    });
}

async function confirmReceiveDelivery(shipmentId) {
    Toast.confirm(
        'Confirm that you have physically received this delivery?',
        async () => {
            try {
                const r = await fetch(`${API}/shipments/status/${shipmentId}?status=DELIVERED`, {
                    method: 'PUT', headers
                });
                if (r.ok) {
                    Toast.show('Delivery confirmed and marked as DELIVERED.', 'success');
                    loadShipments();
                } else {
                    Toast.show(await r.text() || 'Could not confirm delivery.', 'danger');
                }
            } catch { Toast.show('Server error.', 'danger'); }
        },
        null,
        { confirmText: 'Yes, Received', title: 'Confirm Delivery' }
    );
}

// ── Order Modal ───────────────────────────────────────────────────────────────
let _price = 0, _stock = 0;

function openOrderModal(id, productName, price, stock) {
    _price = price;
    _stock = stock;
    const pid = document.getElementById('productId');
    const pn  = document.getElementById('productName');
    const qty = document.getElementById('quantity');
    const si  = document.getElementById('stockInfo');
    if (pid) pid.value = id;
    if (pn)  pn.value  = productName;
    if (qty) qty.value = 1;
    if (si)  si.textContent = `Available stock: ${stock} units`;
    updateTotal();
    new bootstrap.Modal(document.getElementById('orderModal')).show();
}

function updateTotal() {
    const qty = parseInt(document.getElementById('quantity')?.value) || 1;
    const el  = document.getElementById('totalPrice');
    if (el) el.value = '₹ ' + (qty * _price).toFixed(2);
}

async function submitOrder() {
    const qtyEl = document.getElementById('quantity');
    const qty   = parseInt(qtyEl?.value);
    if (!qty || qty < 1) {
        Toast.show('Quantity must be at least 1.', 'warning');
        return;
    }
    if (qty > _stock) {
        Toast.show(`Only ${_stock} units available in stock.`, 'warning');
        return;
    }
    const body = {
        productId:  Number(document.getElementById('productId').value),
        importerId: Number(userId),
        quantity:   qty
    };
    const btn = document.getElementById('submitOrderBtn');
    if (btn) { btn.disabled = true; btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Placing...'; }
    try {
        const r = await fetch(`${API}/orders/create`, {
            method: 'POST', headers, body: JSON.stringify(body)
        });
        if (r.ok) {
            bootstrap.Modal.getInstance(document.getElementById('orderModal'))?.hide();
            Toast.show('Order placed! Waiting for exporter approval.', 'success');
            loadOrders();
        } else {
            const msg = await r.text();
            Toast.show(msg || 'Could not place order.', 'danger');
        }
    } catch { Toast.show('Server error. Please try again.', 'danger'); }
    finally {
        if (btn) { btn.disabled = false; btn.innerHTML = '<i class="bi bi-bag-check me-1"></i>Place Order'; }
    }
}

// ── Payment Modal ─────────────────────────────────────────────────────────────
function openPaymentModal(orderId, amount) {
    const oid = document.getElementById('paymentOrderId');
    const ai  = document.getElementById('paymentAmountInfo');
    if (oid) oid.value       = orderId;
    if (ai)  ai.textContent  = 'Total amount: ₹ ' + amount;
    new bootstrap.Modal(document.getElementById('paymentModal')).show();
}

async function makePayment() {
    const body = {
        orderId:       Number(document.getElementById('paymentOrderId').value),
        paymentMethod: document.getElementById('paymentMethod').value
    };
    const btn = document.getElementById('payNowBtn');
    if (btn) { btn.disabled = true; btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Processing...'; }
    try {
        const r = await fetch(`${API}/payments/pay`, {
            method: 'POST', headers, body: JSON.stringify(body)
        });
        if (r.ok) {
            bootstrap.Modal.getInstance(document.getElementById('paymentModal'))?.hide();
            Toast.show('Payment submitted! Admin will verify and confirm it.', 'success');
            loadPayments();
            loadOrders();
        } else {
            const msg = await r.text();
            Toast.show(msg || 'Payment failed.', 'danger');
        }
    } catch { Toast.show('Payment error. Please try again.', 'danger'); }
    finally {
        if (btn) { btn.disabled = false; btn.innerHTML = '<i class="bi bi-lock-fill me-1"></i>Pay Now'; }
    }
}

// ── Sidebar & Bindings ────────────────────────────────────────────────────────
function bindSidebar() {
    document.getElementById('menuToggle')?.addEventListener('click', () =>
        document.querySelector('.sidebar')?.classList.toggle('show'));
}

function bindActions() {
    document.getElementById('quantity')?.addEventListener('input', updateTotal);

    // Quick action cards
    document.getElementById('openOrders')?.addEventListener   ('click', () => window.location.href = '/orders/my');
    document.getElementById('openPayments')?.addEventListener ('click', () => window.location.href = '/payments/pay');
    document.getElementById('openShipment')?.addEventListener ('click', () => window.location.href = '/shipments/track');

    // Sidebar links
    document.getElementById('myOrdersBtn')?.addEventListener  ('click', () => window.location.href = '/orders/my');
    document.getElementById('paymentBtn')?.addEventListener   ('click', () => window.location.href = '/payments/pay');
    document.getElementById('shipmentBtn')?.addEventListener  ('click', () => window.location.href = '/shipments/track');
    document.getElementById('viewAllOrders')?.addEventListener('click', () => window.location.href = '/orders/my');

    // Cart
    document.getElementById('cartBtn')?.addEventListener('click', () => {
        renderCart();
        new bootstrap.Modal(document.getElementById('cartModal')).show();
    });
}

// ── Logout ────────────────────────────────────────────────────────────────────
function logout() {
    Toast.confirm(
        'Are you sure you want to log out?',
        () => {
            sessionStorage.clear();
            window.location.replace('/login');
        },
        null,
        { confirmText: 'Logout', title: 'Confirm Logout' }
    );
}
