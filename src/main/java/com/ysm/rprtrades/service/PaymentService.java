package com.ysm.rprtrades.service;

import java.util.List;

import com.ysm.rprtrades.dto.PaymentDTO;
import com.ysm.rprtrades.entity.Payment;

public interface PaymentService {

    Payment makePayment(PaymentDTO dto);

    List<Payment> getAllPayments();

    Payment updateStatus(Long id, String status);

    List<Payment> getPaymentsByImporter(Long importerId);

}