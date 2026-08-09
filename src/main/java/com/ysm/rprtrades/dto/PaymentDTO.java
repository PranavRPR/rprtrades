package com.ysm.rprtrades.dto;

import lombok.Data;
@Data
public class PaymentDTO {

    private Long orderId;
    private String paymentMethod;
}