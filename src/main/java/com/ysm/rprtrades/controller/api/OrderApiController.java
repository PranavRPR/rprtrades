package com.ysm.rprtrades.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ysm.rprtrades.dto.OrderDTO;
import com.ysm.rprtrades.entity.Order;
import com.ysm.rprtrades.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    // IMPORTER - PLACE ORDER
    @PreAuthorize("hasRole('IMPORTER')")
    @PostMapping("/create")
    public Order createOrder(@RequestBody OrderDTO dto) {
        return orderService.createOrder(dto);
    }

    // ADMIN - VIEW ALL ORDERS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // IMPORTER - VIEW MY ORDERS
    @GetMapping("/my/{importerId}")
    @PreAuthorize("hasRole('IMPORTER')")
    public List<Order> getMyOrders(@PathVariable Long importerId) {
        return orderService.getOrdersByImporter(importerId);
    }

    // EXPORTER - VIEW ORDERS FOR MY PRODUCTS
    @GetMapping("/exporter/{exporterId}")
    @PreAuthorize("hasAnyRole('EXPORTER','ADMIN')")
    public List<Order> getExporterOrders(@PathVariable Long exporterId) {
        return orderService.getOrdersByExporter(exporterId);
    }

    // EXPORTER / ADMIN - APPROVE ORDER
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER')")
    @PutMapping("/approve/{id}")
    public Order approveOrder(@PathVariable Long id) {
        return orderService.approveOrder(id);
    }

    // EXPORTER / ADMIN - REJECT ORDER
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER')")
    @PutMapping("/reject/{id}")
    public Order rejectOrder(@PathVariable Long id) {
        return orderService.rejectOrder(id);
    }

    // IMPORTER - CANCEL ORDER
    @PreAuthorize("hasRole('IMPORTER')")
    @PutMapping("/cancel/{id}")
    public Order cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }
}
