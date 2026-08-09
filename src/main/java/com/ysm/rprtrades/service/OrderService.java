package com.ysm.rprtrades.service;

import java.util.List;

import com.ysm.rprtrades.dto.OrderDTO;
import com.ysm.rprtrades.entity.Order;

public interface OrderService {

    Order createOrder(OrderDTO dto);

    List<Order> getAllOrders();

    Order approveOrder(Long id);

    Order rejectOrder(Long id);

    Order cancelOrder(Long id);

    List<Order> getOrdersByImporter(Long importerId);

    List<Order> getOrdersByExporter(Long exporterId);
}
