package com.ysm.rprtrades.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysm.rprtrades.dto.ShipmentDTO;
import com.ysm.rprtrades.entity.Order;
import com.ysm.rprtrades.entity.PaymentStatus;
import com.ysm.rprtrades.entity.Shipment;
import com.ysm.rprtrades.entity.ShipmentStatus;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.OrderRepository;
import com.ysm.rprtrades.repository.PaymentRepository;
import com.ysm.rprtrades.repository.ShipmentRepository;
import com.ysm.rprtrades.service.ShipmentService;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Override
    public Shipment createShipment(ShipmentDTO dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        // Guard: payment must be PAID before shipment can be created
        boolean paid = paymentRepo.findByOrderImporterUserId(order.getImporter().getUserId())
                .stream()
                .filter(p -> p.getOrder().getOrderId().equals(order.getOrderId()))
                .anyMatch(p -> p.getStatus() == PaymentStatus.PAID);

        if (!paid) {
            throw new RuntimeException(
                    "Shipment cannot be created until payment for this order is marked as PAID.");
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setCurrentLocation(dto.getCurrentLocation());
        shipment.setTrackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        shipment.setStatus(ShipmentStatus.CREATED);
        return shipmentRepo.save(shipment);
    }

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepo.findAll();
    }

    @Override
    public List<Shipment> getShipmentsByImporter(Long importerId) {
        return shipmentRepo.findByOrderImporterUserId(importerId);
    }

    @Override
    public List<Shipment> getShipmentsByExporter(Long exporterId) {
        return shipmentRepo.findByOrderProductExporterUserId(exporterId);
    }

    @Override
    public Shipment updateStatus(Long shipmentId, String status) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found."));
        shipment.setStatus(ShipmentStatus.valueOf(status.toUpperCase()));
        return shipmentRepo.save(shipment);
    }

    @Override
    public Shipment updateLocation(Long shipmentId, String location) {
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found."));
        shipment.setCurrentLocation(location);
        return shipmentRepo.save(shipment);
    }

    @Override
    public Shipment getShipmentById(Long shipmentId) {
        return shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found."));
    }

    @Override
    public Shipment saveShipment(Shipment shipment) {
        return shipmentRepo.save(shipment);
    }
}
