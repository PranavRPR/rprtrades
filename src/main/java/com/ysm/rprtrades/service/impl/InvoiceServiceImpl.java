package com.ysm.rprtrades.service.impl;

import com.ysm.rprtrades.dto.InvoiceDTO;
import com.ysm.rprtrades.entity.*;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.InvoiceRepository;
import com.ysm.rprtrades.repository.OrderRepository;
import com.ysm.rprtrades.repository.UserRepository;
import com.ysm.rprtrades.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Invoice generateInvoice(InvoiceDTO dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        // Guard: only APPROVED orders can be invoiced
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException("Invoice can only be generated for APPROVED orders.");
        }

        // Guard: prevent duplicate invoice for same order
        if (invoiceRepo.existsByOrderOrderId(order.getOrderId())) {
            throw new RuntimeException("An invoice already exists for this order.");
        }

        User createdBy = userRepo.findById(dto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        double taxPct = 0.0;
        if (dto.getTaxPercentage() != null && !dto.getTaxPercentage().isBlank()) {
            try {
                taxPct = Double.parseDouble(dto.getTaxPercentage().replace("%", "").trim());
            } catch (NumberFormatException e) {
                taxPct = 0.0;
            }
        }

        double amount = order.getTotalAmount();
        double taxAmount = amount * taxPct / 100.0;
        double totalAmount = amount + taxAmount;

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setOrder(order);
        invoice.setCreatedBy(createdBy);
        invoice.setAmount(amount);
        invoice.setTaxPercentage(String.valueOf(taxPct));
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(totalAmount);
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));
        invoice.setStatus(InvoiceStatus.PENDING);

        return invoiceRepo.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }

    @Override
    public List<Invoice> getInvoicesByImporter(Long importerId) {
        return invoiceRepo.findByOrderImporterUserId(importerId);
    }

    @Override
    public List<Invoice> getInvoicesByExporter(Long exporterId) {
        return invoiceRepo.findByOrderProductExporterUserId(exporterId);
    }

    @Override
    public List<Invoice> getInvoicesByOrder(Long orderId) {
        return invoiceRepo.findByOrderOrderId(orderId);
    }

    @Override
    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found."));
    }

    @Override
    public Invoice updateStatus(Long invoiceId, String status) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.setStatus(InvoiceStatus.valueOf(status.toUpperCase()));
        return invoiceRepo.save(invoice);
    }
}