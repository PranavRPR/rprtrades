package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardUIController {

    // ==========================
    // Dashboard Selection Page
    // ==========================
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/admin-dashboard";
    }

    // ==========================
    // Importer Dashboard
    // ==========================
    @GetMapping("/dashboard/importer")
    public String importerDashboard() {
        return "dashboard/importer-dashboard";
    }

    // ==========================
    // Exporter Dashboard
    // ==========================
    @GetMapping("/dashboard/exporter")
    public String exporterDashboard() {
        return "dashboard/exporter-dashboard";
    }

    // ==========================
    // Admin Dashboard
    // ==========================
    @GetMapping("/dashboard/admin")
    public String adminDashboard() {
        return "dashboard/admin-dashboard";
    }

}