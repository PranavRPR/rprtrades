package com.ysm.rprtrades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ysm.rprtrades.entity.Shipment;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    // Importer: shipments for orders they placed
    List<Shipment> findByOrderImporterUserId(Long importerId);

    // Exporter: shipments for orders on their products
    List<Shipment> findByOrderProductExporterUserId(Long exporterId);
}
