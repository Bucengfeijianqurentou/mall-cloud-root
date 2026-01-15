package com.example.order.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal totalPrice;
}
