package com.ysm.rprtrades.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysm.rprtrades.dto.PaymentDTO;
import com.ysm.rprtrades.entity.Order;
import com.ysm.rprtrades.entity.OrderStatus;
import com.ysm.rprtrades.entity.Payment;
import com.ysm.rprtrades.entity.PaymentStatus;
import com.ysm.rprtrades.exception.ResourceNotFoundException;
import com.ysm.rprtrades.repository.OrderRepository;
import com.ysm.rprtrades.repository.PaymentRepository;
import com.ysm.rprtrades.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Override
    public Payment makePayment(PaymentDTO dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        // Guard: payment only allowed for APPROVED orders
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException(
                    "Payment can only be made for APPROVED orders. Current status: " + order.getStatus());
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    @Override
    public Payment updateStatus(Long paymentId, String status) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found."));
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByImporter(Long importerId) {
        return paymentRepo.findByOrderImporterUserId(importerId);
    }
}
