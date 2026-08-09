"use strict";

/**
 * product.js — RPR Trades
 * Handles: products marketplace page (/products)
 * Auth: sessionStorage (tab-isolated)
 * Cart: localStorage keyed by userId (shared across tabs for same user)
 */

const API_BASE = "/api/products";

const token  = sessionStorage.getItem("token");
const userId = sessionStorage.getItem("userId");

const headers = {
    "Content-Type":  "application/json",
    ...(token && { "Authorization": "Bearer " + token })
};

let products         = [];
let filteredProducts = [];
let currentPage      = 1;
const pageSize       = 8;

// ── Cart (localStorage, keyed by userId) ─────────────────────────────────────
const CART_KEY = "rpr_cart_" + (userId || "guest");

function getCart() {
    try { return JSON.parse(localStorage.getItem(CART_KEY)) || []; }
    catch { return []; }
}

function saveCart(cart) {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    updateCartBadge();
}

function updateCartBadge() {
    const badge = document.getElementById("cartCount");
    if (badge) badge.textContent = getCart().length;
}

function addToCart(productId, productName, price, stock) {
    const cart     = getCart();
    const existing = cart.find(i => i.productId === productId);
    if (existing) {
        if (existing.quantity + 1 > stock) {
            Toast.show(`Cannot add more than available stock (${stock}).`, "warning");
            return;
        }
        existing.quantity += 1;
    } else {
        cart.push({ productId, productName, price, stock, quantity: 1 });
    }
    saveCart(cart);
    Toast.show(`${productName} added to cart!`, "success");
}

function removeFromCart(productId) {
    saveCart(getCart().filter(i => i.productId !== productId));
    renderCart();
}

function renderCart() {
    const box = document.getElementById("cartItems");
    if (!box) return;
    const cart = getCart();
    box.innerHTML = "";
    if (!cart.length) {
        box.innerHTML = `
        <div class="text-center text-muted py-4">
            <i class="bi bi-cart-x display-4"></i>
            <p class="mt-2 mb-0">Your cart is empty.</p>
        </div>`;
        return;
    }
    let total = 0;
    cart.forEach(item => {
        const lineTotal = item.price * item.quantity;
        total += lineTotal;
        box.innerHTML += `
        <div class="d-flex justify-content-between align-items-center border-bottom py-2">
            <div>
                <strong>${item.productName}</strong><br>
                <small class="text-muted">₹ ${item.price} × ${item.quantity}</small>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="fw-semibold">₹ ${lineTotal.toFixed(2)}</span>
                <button class="btn btn-outline-danger btn-sm" onclick="removeFromCart(${item.productId})">
                    <i class="bi bi-trash"></i>
                </button>
            </div>
        </div>`;
    });
    box.innerHTML += `
    <div class="d-flex justify-content-between align-items-center mt-3">
        <h5 class="mb-0">Total</h5>
        <h5 class="mb-0 text-success">₹ ${total.toFixed(2)}</h5>
    </div>
    <button class="btn btn-success w-100 mt-3 fw-semibold" onclick="placeOrderFromCart()">
        <i class="bi bi-bag-check me-1"></i>Place All Orders
    </button>`;
}

async function placeOrderFromCart() {
    const cart = getCart();
    if (!cart.length) { Toast.show("Cart is empty.", "warning"); return; }
    if (!userId)      { Toast.show("Please log in as an importer.", "warning"); return; }

    Toast.confirm(
        `Place ${cart.length} order(s) from your cart?`,
        async () => {
            let placed = 0, failed = 0;
            for (const item of cart) {
                const body = { productId: item.productId, importerId: Number(userId), quantity: item.quantity };
                try {
                    const r = await fetch("/api/orders/create", { method: "POST", headers, body: JSON.stringify(body) });
                    if (r.ok) placed++; else failed++;
                } catch { failed++; }
            }
            if (placed > 0) {
                saveCart([]);
                const modal = bootstrap.Modal.getInstance(document.getElementById("cartModal"));
                if (modal) modal.hide();
                Toast.show(`${placed} order(s) placed! Waiting for exporter approval.`, "success");
            }
            if (failed > 0) {
                Toast.show(`${failed} order(s) could not be placed. Check stock or try again.`, "warning");
            }
        },
        null,
        { confirmText: "Place Orders", title: "Confirm Cart Order" }
    );
}

// ── Page load ─────────────────────────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    loadProducts();

    document.getElementById("searchInput")?.addEventListener("keyup", e => {
        if (e.key === "Enter") applyFilters();
    });

    document.getElementById("categoryFilter")?.addEventListener("change", applyFilters);

    document.getElementById("confirmOrderBtn")?.addEventListener("click", placeOrder);

    document.getElementById("cartBtn")?.addEventListener("click", () => {
        renderCart();
        new bootstrap.Modal(document.getElementById("cartModal")).show();
    });

    updateCartBadge();
});

// ── Load products ─────────────────────────────────────────────────────────────
async function loadProducts() {
    showLoading();
    try {
        const r = await fetch(API_BASE, { headers });
        if (!r.ok) throw new Error("Unable to load products.");
        products         = await r.json();
        filteredProducts = [...products];
        renderProducts();
    } catch (error) {
        showError(error.message);
    } finally {
        hideLoading();
    }
}

// ── Filter ────────────────────────────────────────────────────────────────────
function applyFilters() {
    const keyword  = document.getElementById("searchInput")?.value.toLowerCase().trim() || "";
    const category = document.getElementById("categoryFilter")?.value || "";

    filteredProducts = products.filter(p =>
        ((p.productName?.toLowerCase() || "").includes(keyword) ||
         (p.description?.toLowerCase()  || "").includes(keyword)) &&
        (category === "" || p.category === category)
    );
    currentPage = 1;
    renderProducts();
}

function resetFilter() {
    document.getElementById("searchInput").value   = "";
    document.getElementById("categoryFilter").value = "";
    filteredProducts = [...products];
    currentPage = 1;
    renderProducts();
}

// ── Render ────────────────────────────────────────────────────────────────────
function renderProducts() {
    const container = document.getElementById("productContainer");
    const empty     = document.getElementById("emptyProducts");
    if (!container) return;

    container.innerHTML = "";

    if (!filteredProducts.length) {
        if (empty) empty.style.display = "block";
        const pg = document.getElementById("pagination");
        if (pg) pg.innerHTML = "";
        return;
    }

    if (empty) empty.style.display = "none";

    const start = (currentPage - 1) * pageSize;
    filteredProducts.slice(start, start + pageSize).forEach(p => {
        container.innerHTML += createProductCard(p);
    });

    renderPagination();
}

function createProductCard(product) {
    const safeName = (product.productName || "").replace(/'/g, "\\'");
    return `
    <div class="col-xl-3 col-lg-4 col-md-6 mb-4">
        <div class="card product-card h-100">
            <div class="position-relative">
                <img src="https://picsum.photos/400/300?random=${product.productId}"
                     class="product-img w-100" style="height:200px;object-fit:cover;" loading="lazy">
                <div class="badge-status position-absolute top-0 end-0 m-2">
                    ${statusBadge(product.status)}
                </div>
            </div>
            <div class="card-body product-info">
                <h6 class="product-title fw-bold mb-1">${product.productName}</h6>
                <small class="text-muted d-block mb-1">${product.category}</small>
                <p class="product-desc text-muted small mb-2">${truncate(product.description, 60)}</p>
                <div class="d-flex justify-content-between align-items-center mb-0">
                    <span class="fw-bold text-primary fs-5">₹ ${product.price}</span>
                    <small class="text-muted">Stock: ${product.stockQuantity}</small>
                </div>
            </div>
            <div class="card-footer bg-white border-0 pt-0 pb-3 px-3">
                <div class="d-flex gap-2">
                    <button class="btn btn-outline-primary btn-sm flex-fill"
                        onclick="viewProduct(${product.productId})">
                        <i class="bi bi-eye"></i> View
                    </button>
                    <button class="btn btn-outline-success btn-sm flex-fill"
                        onclick="addToCart(${product.productId},'${safeName}',${product.price},${product.stockQuantity})">
                        <i class="bi bi-cart-plus"></i> Cart
                    </button>
                    <button class="btn btn-success btn-sm flex-fill"
                        onclick="openOrderModal(${product.productId})">
                        <i class="bi bi-bag-check"></i> Order
                    </button>
                </div>
            </div>
        </div>
    </div>`;
}

function statusBadge(s) {
    const map = { APPROVED: "success", PENDING: "warning text-dark", REJECTED: "danger" };
    return `<span class="badge bg-${map[s] || "secondary"}">${s}</span>`;
}

// ── Pagination ────────────────────────────────────────────────────────────────
function renderPagination() {
    const pg = document.getElementById("pagination");
    if (!pg) return;
    pg.innerHTML = "";
    const pages = Math.ceil(filteredProducts.length / pageSize);
    for (let i = 1; i <= pages; i++) {
        pg.innerHTML += `
        <li class="page-item ${currentPage === i ? "active" : ""}">
            <button class="page-link" onclick="changePage(${i})">${i}</button>
        </li>`;
    }
}

function changePage(page) {
    currentPage = page;
    renderProducts();
}

// ── Product details modal ─────────────────────────────────────────────────────
async function viewProduct(id) {
    try {
        const r       = await fetch(`${API_BASE}/${id}`, { headers });
        if (!r.ok) throw new Error(await r.text());
        const product = await r.json();

        document.getElementById("modalName").textContent        = product.productName;
        document.getElementById("modalCategory").textContent    = product.category;
        document.getElementById("modalCountry").textContent     = product.countryOfOrigin;
        document.getElementById("modalStock").textContent       = product.stockQuantity;
        document.getElementById("modalStatus").textContent      = product.status;
        document.getElementById("modalDescription").textContent = product.description;
        document.getElementById("modalPrice").textContent       = product.price;

        document.getElementById("buyNowBtn").onclick = function () {
            bootstrap.Modal.getInstance(document.getElementById("productModal"))?.hide();
            openOrderModal(id);
        };

        new bootstrap.Modal(document.getElementById("productModal")).show();
    } catch (error) {
        Toast.show(error.message || "Could not load product details.", "danger");
    }
}

// ── Order modal ───────────────────────────────────────────────────────────────
function openOrderModal(id) {
    document.getElementById("productId").value = id;
    document.getElementById("quantity").value  = 1;
    new bootstrap.Modal(document.getElementById("orderModal")).show();
}

async function placeOrder() {
    if (!userId) {
        Toast.show("Please log in as an importer to place an order.", "warning");
        return;
    }

    const btn  = document.getElementById("confirmOrderBtn");
    const orig = btn?.innerHTML;
    if (btn) { btn.disabled = true; btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span>Placing…'; }

    try {
        const data = {
            productId:  Number(document.getElementById("productId").value),
            importerId: Number(userId),
            quantity:   Number(document.getElementById("quantity").value)
        };

        const r = await fetch("/api/orders/create", { method: "POST", headers, body: JSON.stringify(data) });

        if (!r.ok) throw new Error(await r.text() || "Order failed.");

        bootstrap.Modal.getInstance(document.getElementById("orderModal"))?.hide();
        Toast.show("Order placed successfully! Waiting for exporter approval.", "success");

    } catch (error) {
        Toast.show(error.message, "danger");
    } finally {
        if (btn) { btn.disabled = false; btn.innerHTML = orig || "Place Order"; }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
function truncate(text, length) {
    if (!text) return "";
    return text.length > length ? text.substring(0, length) + "…" : text;
}

function showLoading() {
    const x = document.getElementById("loading");
    if (x) x.style.display = "block";
}

function hideLoading() {
    const x = document.getElementById("loading");
    if (x) x.style.display = "none";
}

// Alias used by inline HTML onclick callbacks in some templates
function showToast(msg, type = "success") { Toast.show(msg, type); }
function showError(msg)   { Toast.show(msg, "danger"); }
function showSuccess(msg) { Toast.show(msg, "success"); }
