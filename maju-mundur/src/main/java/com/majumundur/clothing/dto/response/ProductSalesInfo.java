package com.majumundur.clothing.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class ProductSalesInfo {
    private String productName;
    private int quantity;
    private BigDecimal price;
}