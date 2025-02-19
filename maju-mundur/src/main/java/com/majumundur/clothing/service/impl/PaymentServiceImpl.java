package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.PaymentRequest;
import com.majumundur.clothing.dto.response.PaymentResponse;
import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Order;
import com.majumundur.clothing.entity.Payment;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.entity.enums.OrderStatus;
import com.majumundur.clothing.exception.OrderException;
import com.majumundur.clothing.exception.PaymentException;
import com.majumundur.clothing.repository.CustomerRepository;
import com.majumundur.clothing.repository.OrderRepository;
import com.majumundur.clothing.repository.PaymentRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public CommonResponse<PaymentResponse> makePayment(PaymentRequest request) {
        User user = authenticationService.getLoginUser();
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new PaymentException("Only customers can make payments", 403));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderException("Order not found", 404));

        // Pastikan order ini milik customer yang login
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new PaymentException("This order does not belong to you", 403);
        }

        // Cek apakah order sudah dibayar sebelumnya
        Optional<Payment> existingPayment = paymentRepository.findByOrder(order);
        if (existingPayment.isPresent() && existingPayment.get().getStatus().equals("SUCCESS")) {
            throw new PaymentException("This order has already been paid.", 400);
        }

        // Validasi jumlah pembayaran harus sama dengan total order price
        if (request.getAmount().compareTo(order.getTotalPrice()) != 0) {
            throw new PaymentException("Payment amount must match total order price.", 400);
        }

        Payment payment = Payment.builder()
                .order(order)
                .paymentDate(LocalDateTime.now())
                .amount(order.getTotalPrice()) // Pastikan jumlah sama dengan total order
                .paymentMethod(request.getPaymentMethod())
                .status("SUCCESS")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update status order menjadi "PAID"
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return CommonResponse.<PaymentResponse>builder()
                .statusCode(201)
                .message("Payment completed successfully")
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
