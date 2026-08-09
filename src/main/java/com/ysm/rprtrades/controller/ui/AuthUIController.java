package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthUIController {

    // ==========================
    // Login Page
    // ==========================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ==========================
    // Register Page
    // ==========================
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

}