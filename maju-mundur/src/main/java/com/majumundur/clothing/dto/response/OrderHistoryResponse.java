package com.majumundur.clothing.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderHistoryResponse {
    private String orderId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalPrice;
    private List<ProductOrderInfo> products;

}