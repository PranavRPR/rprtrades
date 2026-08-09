package com.ysm.rprtrades.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private Long totalOrders;
    private Double totalRevenue;
    private Long totalProducts;
    private Long totalShipments;
}