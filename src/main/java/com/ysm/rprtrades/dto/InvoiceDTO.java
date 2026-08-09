package com.ysm.rprtrades.dto;

import lombok.Data;

@Data
public class InvoiceDTO {

    private Long orderId;
    private Long createdById;
    private String taxPercentage;
}