package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeUIController {

    // Landing Page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Home Page
    @GetMapping("/home")
    public String home() {
        return "home";
    }

}