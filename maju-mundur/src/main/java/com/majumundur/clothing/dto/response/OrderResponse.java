package com.majumundur.clothing.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderResponse {
    private String id;
    private String customerId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalPrice;
}
