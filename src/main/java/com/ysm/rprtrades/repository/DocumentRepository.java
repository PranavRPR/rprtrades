package com.ysm.rprtrades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ysm.rprtrades.entity.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByUploadedByUserId(Long userId);

    List<Document> findByOrderOrderId(Long orderId);
}
