package com.ysm.rprtrades.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ysm.rprtrades.dto.ProductDTO;
import com.ysm.rprtrades.entity.Product;
import com.ysm.rprtrades.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductApiController {

    @Autowired
    private ProductService productService;

    // ==========================================
    // EXPORTER - ADD PRODUCT
    // ==========================================

    @PostMapping("/add")
    @PreAuthorize("hasRole('EXPORTER')")
    public Product addProduct(@Valid @RequestBody ProductDTO dto) {

        return productService.addProduct(dto);

    }

    // ==========================================
    // VIEW ALL PRODUCTS
    // ==========================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    public List<Product> getAllProducts() {

        return productService.getAllProducts();

    }

    // ==========================================
    // VIEW PRODUCT BY ID
    // ==========================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    public Product getProductById(@PathVariable Long id) {

        return productService.getProductById(id);

    }

    // ==========================================
    // UPDATE PRODUCT
    // ==========================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPORTER')")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO dto) {

        return productService.updateProduct(id, dto);

    }

    // ==========================================
    // DELETE PRODUCT
    // ==========================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
         return "Product deleted successfully.";
    }

    // ==========================================
    // APPROVE PRODUCT
    // ==========================================

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveProduct(@PathVariable Long id) {

         productService.approveProduct(id);
         return "Product has Been Approved";

    }

    // ==========================================
    // REJECT PRODUCT
    // ==========================================

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product rejectProduct(@PathVariable Long id) {

        return productService.rejectProduct(id);

    }

    // ==========================================
    // PENDING PRODUCTS
    // ==========================================

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Product> getPendingProducts() {

        return productService.getPendingProducts();

    }

    // ==========================================
    // EXPORTER PRODUCTS
    // ==========================================

    @GetMapping("/my/{userId}")
    @PreAuthorize("hasRole('EXPORTER')")
    public List<Product> getMyProducts(
            @PathVariable Long userId) {

        return productService.getProductsByExporter(userId);

    }

    // ==========================================
    // CATEGORY FILTER
    // ==========================================

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    public List<Product> getProductsByCategory(
            @PathVariable String category) {

        return productService.getProductsByCategory(category);

    }

    // ==========================================
    // SEARCH PRODUCT
    // ==========================================

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','EXPORTER','IMPORTER')")
    public List<Product> searchProducts(
            @RequestParam String keyword) {

        return productService.searchProducts(keyword);

    }

}
// Json Form

// Added Product Update
// PUT /api/products/{id}
// ✔ Added Product Reject
// PUT /api/products/reject/{id}

// Admin can reject products.

// ✔ Added Product Search
// GET /api/products/search?keyword=laptop
// ✔ Added Category Filter
// GET /api/products/category/Electronics