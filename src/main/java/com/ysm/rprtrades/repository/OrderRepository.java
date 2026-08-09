package com.ysm.rprtrades.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ysm.rprtrades.entity.Order;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o")
    Double totalRevenue();

    List<Order> findByImporterUserId(Long importerId);

    List<Order> findByProductExporterUserId(Long exporterId);
}
