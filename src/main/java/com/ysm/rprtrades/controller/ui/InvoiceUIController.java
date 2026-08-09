package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvoiceUIController {

    // ==========================================
    // IMPORTER - VIEW MY INVOICES
    // ==========================================
    @GetMapping("/invoices/my")
    public String myInvoices() {
        return "invoice/my-invoices";
    }

    // ==========================================
    // EXPORTER - MANAGE INVOICES
    // ==========================================
    @GetMapping("/invoices/manage")
    public String manageInvoices() {
        return "invoice/manage-invoices";
    }

    // ==========================================
    // ADMIN - VIEW ALL INVOICES
    // ==========================================
    @GetMapping("/invoices")
    public String allInvoices() {
        return "invoice/manage-invoices";
    }
}