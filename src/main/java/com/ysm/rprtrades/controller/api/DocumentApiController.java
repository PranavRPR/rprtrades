package com.ysm.rprtrades.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ysm.rprtrades.entity.Document;
import com.ysm.rprtrades.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentApiController {

    @Autowired
    private DocumentService documentService;

    // IMPORTER / EXPORTER - UPLOAD DOCUMENT
    @PreAuthorize("hasAnyRole('IMPORTER','EXPORTER')")
    @PostMapping("/upload")
    public Document uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadedById") Long uploadedById,
            @RequestParam(value = "orderId", required = false) Long orderId) throws Exception {
        return documentService.uploadFile(file, uploadedById, orderId);
    }

    // ADMIN - VIEW ALL DOCUMENTS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    // IMPORTER / EXPORTER - VIEW MY DOCUMENTS
    @PreAuthorize("hasAnyRole('IMPORTER','EXPORTER')")
    @GetMapping("/my/{userId}")
    public List<Document> getMyDocuments(@PathVariable Long userId) {
        return documentService.getDocumentsByUser(userId);
    }

    // ALL ROLES - DOWNLOAD DOCUMENT
    @PreAuthorize("hasAnyRole('ADMIN','IMPORTER','EXPORTER')")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document doc = documentService.getDocumentById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getFileType() != null ? doc.getFileType() : "application/octet-stream"))
                .body(doc.getData());
    }

    // ADMIN - DELETE DOCUMENT
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return "Document deleted successfully.";
    }
}
