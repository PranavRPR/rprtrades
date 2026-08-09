package com.ysm.rprtrades.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ysm.rprtrades.entity.Product;
import com.ysm.rprtrades.entity.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategory(String category);

    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    List<Product> findByExporterUserId(Long userId);

    List<Product> findByStatusAndCategory(ProductStatus status, String category);

    List<Product> findByStatusOrderByProductIdDesc(ProductStatus status);

    List<Product> findAllByOrderByProductIdDesc();
}
