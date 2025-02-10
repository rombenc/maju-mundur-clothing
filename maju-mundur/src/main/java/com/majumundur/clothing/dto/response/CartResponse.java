package com.majumundur.clothing.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CartResponse {
    private String id;
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
