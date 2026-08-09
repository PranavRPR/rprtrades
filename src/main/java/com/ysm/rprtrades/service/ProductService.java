package com.ysm.rprtrades.service;

import java.util.List;

import com.ysm.rprtrades.dto.ProductDTO;
import com.ysm.rprtrades.entity.Product;

public interface ProductService {

    // ==============================
    // Product CRUD
    // ==============================

    Product addProduct(ProductDTO dto);

    Product updateProduct(Long productId, ProductDTO dto);

    void deleteProduct(Long productId);

    Product getProductById(Long productId);

    List<Product> getAllProducts();

    // ==============================
    // Product Approval
    // ==============================

    Product approveProduct(Long productId);

    Product rejectProduct(Long productId);

    // ==============================
    // Product Filters
    // ==============================

    List<Product> getPendingProducts();

    List<Product> getProductsByExporter(Long userId);

    List<Product> getProductsByCategory(String category);

    List<Product> searchProducts(String keyword);

}