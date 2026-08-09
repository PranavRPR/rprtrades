package com.ysm.rprtrades.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ysm.rprtrades.dto.ReportDTO;
import com.ysm.rprtrades.service.ReportService;
@RestController
@RequestMapping("/api/reports")
public class ReportApiController {

    @Autowired
    private ReportService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ReportDTO getReport() {
        return service.getReport();
    }
}