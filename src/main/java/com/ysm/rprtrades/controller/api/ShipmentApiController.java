package com.ysm.rprtrades.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ysm.rprtrades.dto.ShipmentDTO;
import com.ysm.rprtrades.entity.Shipment;
import com.ysm.rprtrades.service.ShipmentService;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentApiController {

    @Autowired
    private ShipmentService shipmentService;

    // =============================================
    // EXPORTER — CREATE SHIPMENT
    // =============================================
    @PreAuthorize("hasRole('EXPORTER')")
    @PostMapping("/create")
    public ResponseEntity<?> createShipment(@RequestBody ShipmentDTO dto) {
        try {
            return ResponseEntity.ok(shipmentService.createShipment(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =============================================
    // ADMIN — ALL SHIPMENTS
    // =============================================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    // =============================================
    // IMPORTER — MY SHIPMENTS (orders I placed)
    // =============================================
    @PreAuthorize("hasRole('IMPORTER')")
    @GetMapping("/my/{importerId}")
    public List<Shipment> getMyShipments(@PathVariable Long importerId) {
        return shipmentService.getShipmentsByImporter(importerId);
    }

    // =============================================
    // EXPORTER — MY SHIPMENTS (my product orders)
    // =============================================
    @PreAuthorize("hasRole('EXPORTER')")
    @GetMapping("/exporter/{exporterId}")
    public List<Shipment> getExporterShipments(@PathVariable Long exporterId) {
        return shipmentService.getShipmentsByExporter(exporterId);
    }

    // =============================================
    // EXPORTER / ADMIN / IMPORTER — UPDATE STATUS
    // =============================================
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            return ResponseEntity.ok(shipmentService.updateStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =============================================
    // EXPORTER / ADMIN — UPDATE LOCATION
    // =============================================
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER')")
    @PutMapping("/location/{id}")
    public ResponseEntity<?> updateLocation(
            @PathVariable Long id,
            @RequestParam String location) {
        try {
            return ResponseEntity.ok(shipmentService.updateLocation(id, location));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =============================================
    // ALL ROLES — TRACK ONE SHIPMENT
    // =============================================
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> trackShipment(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shipmentService.getShipmentById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
