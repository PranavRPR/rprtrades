package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductUIController {

    @GetMapping
    public String products() {
        return "product/products";
    }

    @GetMapping("/add")
    public String addProduct() {
        return "product/add-product";
    }

    @GetMapping("/my")
    public String myProducts() {
        return "product/my-products";
    }

    @GetMapping("/pending")
    public String pendingProducts() {
        return "product/pending-products";
    }
}
