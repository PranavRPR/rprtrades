package com.ysm.rprtrades.service.impl;

import com.ysm.rprtrades.dto.ReportDTO;
import com.ysm.rprtrades.repository.OrderRepository;
import com.ysm.rprtrades.repository.ProductRepository;
import com.ysm.rprtrades.repository.ShipmentRepository;
import com.ysm.rprtrades.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ShipmentRepository shipmentRepo;

    @Override
    public ReportDTO getReport() {
        long orders    = orderRepo.count();
        Double revenue = orderRepo.totalRevenue();
        long products  = productRepo.count();
        long shipments = shipmentRepo.count();

        if (revenue == null) revenue = 0.0;

        return new ReportDTO(orders, revenue, products, shipments);
    }
}
