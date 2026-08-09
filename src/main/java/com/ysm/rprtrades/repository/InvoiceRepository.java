package com.ysm.rprtrades.repository;

import com.ysm.rprtrades.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByOrderImporterUserId(Long importerId);

    List<Invoice> findByOrderProductExporterUserId(Long exporterId);

    List<Invoice> findByOrderOrderId(Long orderId);

    boolean existsByOrderOrderId(Long orderId);
}