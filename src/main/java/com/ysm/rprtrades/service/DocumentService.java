package com.ysm.rprtrades.service;

import com.ysm.rprtrades.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    Document uploadFile(MultipartFile file, Long uploadedById, Long orderId) throws Exception;

    List<Document> getAllDocuments();

    List<Document> getDocumentsByUser(Long userId);

    Document getDocumentById(Long id);

    void deleteDocument(Long id);
}
