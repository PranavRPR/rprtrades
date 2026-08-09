package com.ysm.rprtrades.service.impl;

import com.ysm.rprtrades.entity.Document;
import com.ysm.rprtrades.entity.Order;
import com.ysm.rprtrades.entity.User;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.DocumentRepository;
import com.ysm.rprtrades.repository.OrderRepository;
import com.ysm.rprtrades.repository.UserRepository;
import com.ysm.rprtrades.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Document uploadFile(MultipartFile file, Long uploadedById, Long orderId) throws Exception {
        Document doc = new Document();
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setData(file.getBytes());

        if (uploadedById != null) {
            User uploader = userRepository.findById(uploadedById).orElse(null);
            doc.setUploadedBy(uploader);
        }

        if (orderId != null) {
            Order order = orderRepository.findById(orderId).orElse(null);
            doc.setOrder(order);
        }

        return repo.save(doc);
    }

    @Override
    public List<Document> getAllDocuments() {
        return repo.findAll();
    }

    @Override
    public List<Document> getDocumentsByUser(Long userId) {
        return repo.findByUploadedByUserId(userId);
    }

    @Override
    public Document getDocumentById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found."));
    }

    @Override
    public void deleteDocument(Long id) {
        repo.delete(getDocumentById(id));
    }
}
