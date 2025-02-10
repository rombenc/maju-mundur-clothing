package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.PaymentRequest;
import com.majumundur.clothing.dto.response.PaymentResponse;
import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Order;
import com.majumundur.clothing.entity.Payment;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.exception.OrderException;
import com.majumundur.clothing.exception.PaymentException;
import com.majumundur.clothing.repository.OrderRepository;
import com.majumundur.clothing.repository.PaymentRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;

    @Override
    public CommonResponse<PaymentResponse> makePayment(PaymentRequest request) {
        User user = authenticationService.getLoginUser();
        Customer customer = user.getCustomer();

        if (customer == null) {
            throw new PaymentException("Only customers can make payments", 403);
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderException("Order not found", 404));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new PaymentException("This order does not belong to you", 403);
        }

        Payment payment = Payment.builder()
                .order(order)
                .paymentDate(LocalDateTime.now())
                .amount(order.getTotalPrice())
                .paymentMethod(request.getPaymentMethod())
                .status("PENDING")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return CommonResponse.<PaymentResponse>builder()
                .statusCode(201)
                .message("Payment initiated successfully")
                .data(toPaymentResponse(savedPayment))
                .build();
    }

    @Override
    public CommonResponse<List<PaymentResponse>> getPaymentsByCustomer() {
        User user = authenticationService.getLoginUser();
        Customer customer = user.getCustomer();

        if (customer == null) {
            throw new PaymentException("Only customers can view payments", 403);
        }

        List<PaymentResponse> payments = paymentRepository.findByOrder_Customer(customer)
                .stream()
                .map(this::toPaymentResponse)
                .toList();

        return CommonResponse.<List<PaymentResponse>>builder()
                .statusCode(200)
                .message("Successfully fetched payments")
                .data(payments)
                .build();
    }

    @Override
    public CommonResponse<PaymentResponse> getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found", 404));

        return CommonResponse.<PaymentResponse>builder()
                .statusCode(200)
                .message("Successfully fetched payment")
                .data(toPaymentResponse(payment))
                .build();
    }

    @Override
    public CommonResponse<PaymentResponse> updatePaymentStatus(String paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found", 404));

        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);

        return CommonResponse.<PaymentResponse>builder()
                .statusCode(200)
                .message("Payment status updated successfully")
                .data(toPaymentResponse(updatedPayment))
                .build();
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .build();
    }
}
