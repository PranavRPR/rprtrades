package com.ysm.rprtrades.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysm.rprtrades.dto.ProductDTO;
import com.ysm.rprtrades.entity.Product;
import com.ysm.rprtrades.entity.ProductStatus;
import com.ysm.rprtrades.entity.User;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.ProductRepository;
import com.ysm.rprtrades.repository.UserRepository;
import com.ysm.rprtrades.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Product addProduct(ProductDTO dto) {
        User exporter = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Exporter not found."));
        Product product = new Product();
        product.setProductName(dto.getProductName().trim());
        product.setCategory(dto.getCategory().trim());
        product.setDescription(dto.getDescription().trim());
        product.setPrice(dto.getPrice());
        product.setCountryOfOrigin(dto.getCountryOfOrigin().trim());
        product.setStockQuantity(dto.getStockQuantity());
        product.setStatus(ProductStatus.PENDING);
        product.setExporter(exporter);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, ProductDTO dto) {
        Product product = getProductById(productId);
        product.setProductName(dto.getProductName().trim());
        product.setCategory(dto.getCategory().trim());
        product.setDescription(dto.getDescription().trim());
        product.setPrice(dto.getPrice());
        product.setCountryOfOrigin(dto.getCountryOfOrigin().trim());
        product.setStockQuantity(dto.getStockQuantity());
        product.setStatus(ProductStatus.PENDING);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByProductIdDesc();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    @Override
    public Product approveProduct(Long productId) {
        Product product = getProductById(productId);
        product.setStatus(ProductStatus.APPROVED);
        return productRepository.save(product);
    }

    @Override
    public Product rejectProduct(Long productId) {
        Product product = getProductById(productId);
        product.setStatus(ProductStatus.REJECTED);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.delete(getProductById(productId));
    }

    @Override
    public List<Product> getPendingProducts() {
        return productRepository.findByStatusOrderByProductIdDesc(ProductStatus.PENDING);
    }

    @Override
    public List<Product> getProductsByExporter(Long userId) {
        return productRepository.findByExporterUserId(userId);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }
}
