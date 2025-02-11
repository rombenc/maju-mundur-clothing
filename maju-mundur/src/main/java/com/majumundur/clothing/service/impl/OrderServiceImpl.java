package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.OrderRequest;
import com.majumundur.clothing.dto.response.OrderResponse;
import com.majumundur.clothing.entity.*;
import com.majumundur.clothing.exception.OrderException;
import com.majumundur.clothing.exception.ProductException;
import com.majumundur.clothing.repository.CartRepository;
import com.majumundur.clothing.repository.OrderRepository;
import com.majumundur.clothing.repository.ProductRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.OrderService;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AuthenticationService authenticationService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CommonResponse<OrderResponse> createOrder(OrderRequest request) {
        User user = authenticationService.getLoginUser();
        Customer customer = user.getCustomer();

        if (customer == null) {
            throw new OrderException("Only customers can place orders", 403);
        }

        // Ambil hanya cart milik customer yang belum di-checkout
        List<Cart> cartItems = cartRepository.findByCustomerAndOrderIsNull(customer);

        if (cartItems.isEmpty()) {
            throw new OrderException("Cart is empty", 400);
        }

        validateStock(cartItems);

        BigDecimal totalPrice = cartItems.stream()
                .map(Cart::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalPrice(totalPrice)
                .build();

        Order savedOrder = orderRepository.save(order);

        // Hubungkan semua cart dengan order yang baru dibuat
        cartItems.forEach(cart -> cart.setOrder(savedOrder));
        cartRepository.saveAll(cartItems);

        // Setelah order sukses tersimpan, baru kurangi stok
        cartItems.forEach(this::reduceProductStock);

        return CommonResponse.<OrderResponse>builder()
                .statusCode(201)
                .message("Order successfully created")
                .data(toOrderResponse(savedOrder))
                .build();
    }


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    private void reduceProductStock(Cart cartItem) {
        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new ProductException("Product not found", 404));

        if (product.getStock() < cartItem.getQuantity()) {
            throw new OrderException("Insufficient stock for product: " + product.getName(), 400);
        }

        product.setStock(product.getStock() - cartItem.getQuantity());
        productRepository.save(product);
    }

    private void validateStock(List<Cart> cartItems) {
        cartItems.forEach(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new OrderException("Insufficient stock for product: " + product.getName(), 400);
            }
        });
    }

    @Transactional
    public void rollbackOrderStock(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found", 404));

        order.getCarts().forEach(cartItem -> {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() + cartItem.getQuantity());
            productRepository.save(product);
        });
    }

    @Override
    public CommonResponse<List<OrderResponse>> getOrdersByCustomer() {
        User user = authenticationService.getLoginUser();
        Customer customer = user.getCustomer();

        if (customer == null) {
            throw new OrderException("Only customers can view orders", 403);
        }

        List<OrderResponse> orders = orderRepository.findByCustomer(customer)
                .stream()
                .map(this::toOrderResponse)
                .toList();

        return CommonResponse.<List<OrderResponse>>builder()
                .statusCode(200)
                .message("Successfully fetched orders")
                .data(orders)
                .build();
    }

    @Override
    public CommonResponse<OrderResponse> getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found", 404));

        return CommonResponse.<OrderResponse>builder()
                .statusCode(200)
                .message("Successfully fetched order")
                .data(toOrderResponse(order))
                .build();
    }

    @Override
    public CommonResponse<OrderResponse> updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found", 404));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return CommonResponse.<OrderResponse>builder()
                .statusCode(200)
                .message("Order status updated successfully")
                .data(toOrderResponse(updatedOrder))
                .build();
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
