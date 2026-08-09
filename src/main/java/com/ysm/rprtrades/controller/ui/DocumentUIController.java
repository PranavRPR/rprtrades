package com.ysm.rprtrades.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentUIController {

    // ==========================================
    // ADMIN - MANAGE ALL DOCUMENTS
    // ==========================================
    @GetMapping("/documents")
    public String documents() {
        return "admin/documents";
    }

    // ==========================================
    // IMPORTER - MY DOCUMENTS
    // ==========================================
    @GetMapping("/documents/my")
    public String myDocuments() {
        return "document/my-documents";
    }

    // ==========================================
    // EXPORTER - UPLOAD / MANAGE DOCUMENTS
    // ==========================================
    @GetMapping("/documents/upload")
    public String uploadDocument() {
        return "document/exporter-documents";
    }

    // ==========================================
    // EXPORTER - MY DOCUMENTS
    // ==========================================
    @GetMapping("/documents/exporter")
    public String exporterDocuments() {
        return "document/exporter-documents";
    }
}