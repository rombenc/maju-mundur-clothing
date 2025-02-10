package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.PaymentRequest;
import com.majumundur.clothing.dto.response.PaymentResponse;
import com.majumundur.clothing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CommonResponse<PaymentResponse>> makePayment(@RequestBody PaymentRequest request) {
        CommonResponse<PaymentResponse> response = paymentService.makePayment(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<PaymentResponse>>> getPayments() {
        CommonResponse<List<PaymentResponse>> response = paymentService.getPaymentsByCustomer();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<PaymentResponse>> getPaymentById(@PathVariable String paymentId) {
        CommonResponse<PaymentResponse> response = paymentService.getPaymentById(paymentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<PaymentResponse>> updatePaymentStatus(@PathVariable String paymentId, @RequestParam String status) {
        CommonResponse<PaymentResponse> response = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
