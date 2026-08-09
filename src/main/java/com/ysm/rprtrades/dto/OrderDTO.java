package com.ysm.rprtrades.dto;


import lombok.Data;

@Data
public class OrderDTO {

    private Long productId;
    private Long importerId;
    private Integer quantity;
}