package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentUIController {

    @GetMapping("/payments")
    public String payments() {
        return "payment/payment-history";
    }

    @GetMapping("/payments/pay")
    public String pay() {
        return "payment/payment";
    }

    @GetMapping("/payments/manage")
    public String managePayments() {
        return "payment/manage-payments";
    }

}