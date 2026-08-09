package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderUIController {

    @GetMapping("/orders")
    public String orders() {
        return "order/orders";
    }

    @GetMapping("/orders/create")
    public String createOrder() {
        return "order/create-order";
    }

    @GetMapping("/orders/my")
    public String myOrders() {
        return "order/my-orders";
    }

    // Exporter: dedicated page for orders received on their products
    @GetMapping("/orders/exporter")
    public String exporterOrders() {
        return "order/exporter-orders";
    }

    // Pending orders (shared by exporter and admin)
    @GetMapping("/orders/pending")
    public String pendingOrders() {
        return "order/pending-orders";
    }
}
