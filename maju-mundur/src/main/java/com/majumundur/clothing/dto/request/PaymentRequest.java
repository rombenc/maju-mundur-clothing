package com.majumundur.clothing.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequest {
    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Payment amount is required")
    @Min(value = 1, message = "Payment amount must be at least 1")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
}
