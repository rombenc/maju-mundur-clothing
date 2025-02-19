package com.majumundur.clothing.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ProductOrderInfo {
    private String productName;
    private int quantity;
    private BigDecimal price;
}