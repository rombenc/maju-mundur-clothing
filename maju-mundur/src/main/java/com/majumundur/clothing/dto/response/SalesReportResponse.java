package com.majumundur.clothing.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SalesReportResponse {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalPrice;
    private List<ProductSalesInfo> products;
}
