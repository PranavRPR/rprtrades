package com.ysm.rprtrades.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ysm.rprtrades.dto.PaymentDTO;
import com.ysm.rprtrades.entity.Payment;
import com.ysm.rprtrades.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentApiController {

    @Autowired
    private PaymentService paymentService;

    // ==========================================
    // IMPORTER - MAKE PAYMENT
    // ==========================================
    @PreAuthorize("hasRole('IMPORTER')")
    @PostMapping("/pay")
    public Payment makePayment(@RequestBody PaymentDTO dto) {
        return paymentService.makePayment(dto);
    }

    // ==========================================
    // ADMIN - VIEW ALL PAYMENTS
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/my/{importerId}")
    @PreAuthorize("hasRole('IMPORTER')")
    public List<Payment> getMyPayments(@PathVariable Long importerId) {
        return paymentService.getPaymentsByImporter(importerId);
    }

    // ==========================================
    // ADMIN - UPDATE PAYMENT STATUS
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public Payment updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return paymentService.updateStatus(id, status);
    }
}