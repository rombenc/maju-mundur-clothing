package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.OrderRequest;
import com.majumundur.clothing.dto.response.OrderResponse;
import com.majumundur.clothing.entity.Cart;
import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Order;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.exception.OrderException;
import com.majumundur.clothing.repository.CartRepository;
import com.majumundur.clothing.repository.OrderRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AuthenticationService authenticationService;

    @Override
    public CommonResponse<OrderResponse> createOrder(OrderRequest request) {
        User user = authenticationService.getLoginUser();
        Customer customer = user.getCustomer();

        if (customer == null) {
            throw new OrderException("Only customers can place orders", 403);
        }

        List<Cart> cartItems = cartRepository.findAll();

        if (cartItems.isEmpty()) {
            throw new OrderException("Cart is empty", 400);
        }

        BigDecimal totalPrice = cartItems.stream()
                .map(Cart::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .totalPrice(totalPrice)
                .carts(cartItems)
                .build();

        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteAll(cartItems);

        return CommonResponse.<OrderResponse>builder()
                .statusCode(201)
                .message("Order successfully created")
                .data(toOrderResponse(savedOrder))
                .build();
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
