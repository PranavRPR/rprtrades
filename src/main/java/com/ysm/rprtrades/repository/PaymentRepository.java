package com.ysm.rprtrades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ysm.rprtrades.entity.Payment;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderImporterUserId(Long userId);

}