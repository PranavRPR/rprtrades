package com.ysm.rprtrades.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysm.rprtrades.dto.OrderDTO;
import com.ysm.rprtrades.entity.Order;
import com.ysm.rprtrades.entity.OrderStatus;
import com.ysm.rprtrades.entity.Product;
import com.ysm.rprtrades.entity.ProductStatus;
import com.ysm.rprtrades.entity.User;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.OrderRepository;
import com.ysm.rprtrades.repository.ProductRepository;
import com.ysm.rprtrades.repository.UserRepository;
import com.ysm.rprtrades.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public Order createOrder(OrderDTO dto) {
        User importer = userRepo.findById(dto.getImporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Importer not found."));
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        // Guard: product must be APPROVED
        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new RuntimeException("Cannot order a product that is not approved.");
        }
        // Guard: sufficient stock
        if (product.getStockQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        Order order = new Order();
        order.setImporter(importer);
        order.setProduct(product);
        order.setQuantity(dto.getQuantity());
        order.setTotalAmount(product.getPrice() * dto.getQuantity());
        order.setStatus(OrderStatus.PENDING);

        Order saved = orderRepo.save(order);

        // Deduct stock
        product.setStockQuantity(product.getStockQuantity() - dto.getQuantity());
        productRepo.save(product);

        return saved;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Order approveOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        order.setStatus(OrderStatus.APPROVED);
        return orderRepo.save(order);
    }

    @Override
    public Order rejectOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        // Restore stock on rejection
        Product product = order.getProduct();
        product.setStockQuantity(product.getStockQuantity() + order.getQuantity());
        productRepo.save(product);
        order.setStatus(OrderStatus.REJECTED);
        return orderRepo.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        // Only PENDING or APPROVED orders can be cancelled before shipping
        if (order.getStatus() != OrderStatus.PENDING
                && order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException("Only PENDING or APPROVED orders can be cancelled.");
        }

        // Restore stock back to the product
        Product product = order.getProduct();
        product.setStockQuantity(product.getStockQuantity() + order.getQuantity());
        productRepo.save(product);

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    @Override
    public List<Order> getOrdersByImporter(Long importerId) {
        return orderRepo.findByImporterUserId(importerId);
    }

    @Override
    public List<Order> getOrdersByExporter(Long exporterId) {
        return orderRepo.findByProductExporterUserId(exporterId);
    }
}
