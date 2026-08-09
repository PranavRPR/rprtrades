package com.ysm.rprtrades.controller.api;

import com.ysm.rprtrades.dto.InvoiceDTO;
import com.ysm.rprtrades.entity.Invoice;
import com.ysm.rprtrades.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceApiController {

    @Autowired
    private InvoiceService invoiceService;

    // ==========================================
    // EXPORTER / ADMIN - GENERATE INVOICE
    // ==========================================
    @PreAuthorize("hasAnyRole('EXPORTER','ADMIN')")
    @PostMapping("/generate")
    public ResponseEntity<?> generateInvoice(@RequestBody InvoiceDTO dto) {
        try {
            return ResponseEntity.ok(invoiceService.generateInvoice(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // ADMIN - VIEW ALL INVOICES
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    // ==========================================
    // IMPORTER - VIEW MY INVOICES
    // ==========================================
    @PreAuthorize("hasRole('IMPORTER')")
    @GetMapping("/my/{importerId}")
    public List<Invoice> getMyInvoices(@PathVariable Long importerId) {
        return invoiceService.getInvoicesByImporter(importerId);
    }

    // ==========================================
    // EXPORTER - VIEW INVOICES FOR MY PRODUCTS
    // ==========================================
    @PreAuthorize("hasRole('EXPORTER')")
    @GetMapping("/exporter/{exporterId}")
    public List<Invoice> getExporterInvoices(@PathVariable Long exporterId) {
        return invoiceService.getInvoicesByExporter(exporterId);
    }

    // ==========================================
    // ALL ROLES - VIEW INVOICES FOR AN ORDER
    // ==========================================
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    @GetMapping("/order/{orderId}")
    public List<Invoice> getInvoicesByOrder(@PathVariable Long orderId) {
        return invoiceService.getInvoicesByOrder(orderId);
    }

    // ==========================================
    // ALL ROLES - VIEW ONE INVOICE
    // ==========================================
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(invoiceService.getInvoiceById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // ADMIN / EXPORTER - UPDATE INVOICE STATUS
    // ==========================================
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER')")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            return ResponseEntity.ok(invoiceService.updateStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}