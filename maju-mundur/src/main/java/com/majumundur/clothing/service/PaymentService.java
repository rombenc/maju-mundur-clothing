package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.PaymentRequest;
import com.majumundur.clothing.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    CommonResponse<PaymentResponse> makePayment(PaymentRequest request);
    CommonResponse<List<PaymentResponse>> getPaymentsByCustomer();
    CommonResponse<PaymentResponse> getPaymentById(String paymentId);
    CommonResponse<PaymentResponse> updatePaymentStatus(String paymentId, String status);
}
