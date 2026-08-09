package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShipmentUIController {

    @GetMapping("/shipments")
    public String shipments() {
        return "shipment/manage-shipments";
    }

    @GetMapping("/shipments/create")
    public String createShipment() {
        return "shipment/create-shipment";
    }

    @GetMapping("/shipments/track")
    public String trackShipment() {
        return "shipment/track-shipment";
    }

    @GetMapping("/shipments/manage")
    public String manageShipments() {
        return "shipment/manage-shipments";
    }
}
