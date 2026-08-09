package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUIController {

    // ==========================================
    // USER MANAGEMENT
    // ==========================================
    @GetMapping("/admin/users")
    public String users() {
        return "admin/users";
    }

    // ==========================================
    // PRODUCT APPROVAL
    // ==========================================
    @GetMapping("/admin/products")
    public String products() {
        return "product/pending-products";
    }

    // ==========================================
    // ORDER MANAGEMENT
    // ==========================================
    @GetMapping("/admin/orders")
    public String orders() {
        return "order/orders";
    }

    // ==========================================
    // PAYMENT MANAGEMENT
    // ==========================================
    @GetMapping("/admin/payments")
    public String payments() {
        return "payment/manage-payments";
    }

    // ==========================================
    // SHIPMENT MANAGEMENT
    // ==========================================
    @GetMapping("/admin/shipments")
    public String shipments() {
        return "shipment/manage-shipments";
    }

    // ==========================================
    // DOCUMENT MANAGEMENT
    // ==========================================
    @GetMapping("/admin/documents")
    public String documents() {
        return "admin/documents";
    }

    // ==========================================
    // REPORTS
    // ==========================================
    @GetMapping("/admin/reports")
    public String reports() {
        return "admin/reports";
    }

    // ==========================================
    // SYSTEM MONITORING & SECURITY
    // ==========================================
    @GetMapping("/admin/monitoring")
    public String monitoring() {
        return "admin/monitoring";
    }

    // ==========================================
    // INVOICE MANAGEMENT
    // ==========================================
    @GetMapping("/admin/invoices")
    public String invoices() {
        return "invoice/manage-invoices";
    }

}
