package com.ysm.rprtrades.service;

import com.ysm.rprtrades.dto.ShipmentDTO;
import com.ysm.rprtrades.entity.Shipment;
import java.util.List;

public interface ShipmentService {

    Shipment createShipment(ShipmentDTO dto);

    List<Shipment> getAllShipments();

    List<Shipment> getShipmentsByImporter(Long importerId);

    List<Shipment> getShipmentsByExporter(Long exporterId);

    Shipment updateStatus(Long shipmentId, String status);

    Shipment updateLocation(Long shipmentId, String location);

    Shipment getShipmentById(Long shipmentId);

    Shipment saveShipment(Shipment shipment);
}
