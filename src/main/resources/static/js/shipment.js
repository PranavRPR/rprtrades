/**
 * shipment.js — RPR Trades
 * Handles: create-shipment form, track-shipment page, manage-shipments page
 * Auth: sessionStorage (tab-isolated)
 */

const API    = "/api";
const token  = sessionStorage.getItem("token");
const userId = sessionStorage.getItem("userId");
const role   = sessionStorage.getItem("role");

const headers = {
    "Authorization": "Bearer " + token,
    "Content-Type":  "application/json"
};

// ── Status badge ──────────────────────────────────────────────────────────────
function statusBadge(s) {
    if (!s) return '<span class="badge bg-secondary">—</span>';
    const map = {
        CREATED:    "warning text-dark",
        IN_TRANSIT: "info text-dark",
        DELIVERED:  "success",
        SHIPPED:    "info text-dark"
    };
    return `<span class="badge bg-${map[s.toUpperCase()] || "secondary"}">${s.replace(/_/g, " ")}</span>`;
}

// ── Load orders for shipment form (create-shipment.html) ──────────────────────
async function loadOrdersForShipment() {
    const select = document.getElementById("orderId");
    if (!select) return;

    select.innerHTML = "<option value=''>Loading approved orders…</option>";

    try {
        const r      = await fetch(`${API}/orders/exporter/${userId}`, { headers });
        if (!r.ok) throw new Error(await r.text());
        const orders = await r.json();

        const approved = orders.filter(o => o.status === "APPROVED");
        if (!approved.length) {
            select.innerHTML = "<option value=''>No approved orders available</option>";
            return;
        }

        select.innerHTML = "<option value=''>— Select an Order —</option>";
        approved.forEach(o => {
            select.innerHTML += `<option value="${o.orderId}">
                Order #${o.orderId} — ${o.product?.productName || "?"} (₹ ${o.totalAmount})
            </option>`;
        });
    } catch (e) {
        select.innerHTML = "<option value=''>Could not load orders</option>";
        Toast.show("Failed to load orders: " + e.message, "danger");
    }
}

// ── Shipment form submit (create-shipment.html) ───────────────────────────────
document.getElementById("shipmentForm")?.addEventListener("submit", async function (e) {
    e.preventDefault();

    const orderIdEl   = document.getElementById("orderId");
    const locationEl  = document.getElementById("currentLocation");
    const btn         = document.getElementById("createBtn");
    const orig        = btn?.innerHTML;

    if (!orderIdEl?.value) {
        Toast.show("Please select an order.", "warning");
        return;
    }

    if (btn) { btn.disabled = true; btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Creating…'; }

    const data = {
        orderId:         Number(orderIdEl.value),
        currentLocation: locationEl?.value?.trim() || ""
    };

    try {
        const r = await fetch(`${API}/shipments/create`, {
            method: "POST",
            headers,
            body:   JSON.stringify(data)
        });

        if (r.ok) {
            const ship = await r.json();
            Toast.show(`Shipment created! Tracking: <strong>${ship.trackingNumber}</strong>`, "success");
            this.reset();
            const preview = document.getElementById("orderPreview");
            if (preview) preview.style.display = "none";
            setTimeout(() => window.location.href = "/shipments/manage", 1800);
        } else {
            const err = await r.text();
            Toast.show(err || "Failed to create shipment.", "danger");
        }
    } catch (e) {
        Toast.show("Server error. Please try again.", "danger");
    } finally {
        if (btn) { btn.disabled = false; btn.innerHTML = orig || "Create Shipment"; }
    }
});

// ── Track shipment page (track-shipment.html / importer) ─────────────────────
async function trackShipment() {
    const table = document.getElementById("shipmentTrackTable");
    if (!table) return;

    table.innerHTML = `<tr><td colspan="5" class="text-center py-4">
        <span class="spinner-border spinner-border-sm me-2"></span>Loading shipments…
    </td></tr>`;

    try {
        const r         = await fetch(`${API}/shipments/my/${userId}`, { headers });
        if (!r.ok) throw new Error(await r.text());
        const shipments = await r.json();

        table.innerHTML = "";
        if (!shipments.length) {
            table.innerHTML = `<tr><td colspan="5" class="text-center py-4 text-muted">No shipments found.</td></tr>`;
            return;
        }

        shipments.forEach(s => {
            let actionBtn = '<span class="text-muted small">—</span>';
            if (s.status === "IN_TRANSIT" || s.status === "CREATED" || s.status === "SHIPPED") {
                actionBtn = `<button class="btn btn-success btn-sm fw-semibold" onclick="receiveDelivery(${s.shipmentId})">
                    <i class="bi bi-box-seam me-1"></i>Received
                </button>`;
            } else if (s.status === "DELIVERED") {
                actionBtn = `<span class="badge bg-success"><i class="bi bi-check-all me-1"></i>Delivered</span>`;
            }

            table.innerHTML += `
            <tr>
                <td><code>${s.trackingNumber || "—"}</code></td>
                <td>#${s.order?.orderId || "—"}</td>
                <td>${s.currentLocation || "—"}</td>
                <td>${statusBadge(s.status)}</td>
                <td class="text-center">${actionBtn}</td>
            </tr>`;
        });
    } catch (e) {
        table.innerHTML = `<tr><td colspan="5" class="text-center text-danger py-4">${e.message}</td></tr>`;
    }
}

async function receiveDelivery(shipmentId) {
    Toast.confirm(
        "Confirm that you have physically received this delivery?",
        async () => {
            try {
                const r = await fetch(`${API}/shipments/status/${shipmentId}?status=DELIVERED`, {
                    method: "PUT",
                    headers
                });
                if (r.ok) {
                    Toast.show("Delivery confirmed! Status updated to DELIVERED.", "success");
                    trackShipment();
                } else {
                    Toast.show(await r.text() || "Unable to confirm delivery.", "danger");
                }
            } catch {
                Toast.show("Server error. Please try again.", "danger");
            }
        },
        null,
        { confirmText: "Yes, Received", title: "Confirm Delivery" }
    );
}

// ── Auto-init ─────────────────────────────────────────────────────────────────
if (document.getElementById("orderId"))              loadOrdersForShipment();
if (document.getElementById("shipmentTrackTable"))   trackShipment();
