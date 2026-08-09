package com.ysm.rprtrades.service;

import com.ysm.rprtrades.dto.InvoiceDTO;
import com.ysm.rprtrades.entity.Invoice;

import java.util.List;

public interface InvoiceService {

    Invoice generateInvoice(InvoiceDTO dto);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByImporter(Long importerId);

    List<Invoice> getInvoicesByExporter(Long exporterId);

    List<Invoice> getInvoicesByOrder(Long orderId);

    Invoice getInvoiceById(Long invoiceId);

    Invoice updateStatus(Long invoiceId, String status);
}