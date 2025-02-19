package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.OrderRequest;
import com.majumundur.clothing.dto.response.OrderHistoryResponse;
import com.majumundur.clothing.dto.response.OrderResponse;
import com.majumundur.clothing.dto.response.ProductOrderInfo;
import com.majumundur.clothing.dto.response.SalesReportResponse;
import com.majumundur.clothing.entity.*;
import com.majumundur.clothing.entity.enums.OrderStatus;
import com.majumundur.clothing.exception.OrderException;
import com.majumundur.clothing.exception.ProductException;
import com.majumundur.clothing.exception.UnauthorizedException;
import com.majumundur.clothing.repository.CartRepository;
import com.majumundur.clothing.repository.OrderRepository;
import com.majumundur.clothing.repository.ProductRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .status(OrderStatus.PENDING)
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


    private void reduceProductStock(Cart cartItem) {
        Product product = productRepository.findByIdForUpdate(cartItem.getProduct().getId())
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

    @Transactional(rollbackFor = OrderException.class)
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

        order.setStatus(OrderStatus.valueOf(status));
        Order updatedOrder = orderRepository.save(order);

        return CommonResponse.<OrderResponse>builder()
                .statusCode(200)
                .message("Order status updated successfully")
                .data(toOrderResponse(updatedOrder))
                .build();
    }

    @Override
    public List<OrderHistoryResponse> getCustomerOrderHistory() {
        User user = authenticationService.getLoginUser();
        Customer customer = user.getCustomer();

        if (customer == null) {
            throw new OrderException("Only customers can access order history", 403);
        }

        List<Order> orders = orderRepository.findOrdersByCustomerId(customer.getId());

        return orders.stream().map(order -> new OrderHistoryResponse(
                order.getId(),
                order.getOrderDate(),
                String.valueOf(order.getStatus()),
                order.getTotalPrice(),
                order.getCarts().stream().map(cart -> new ProductOrderInfo(
                        cart.getProduct().getName(),
                        cart.getQuantity(),
                        cart.getPrice()
                )).toList()
        )).toList();
    }

    @Override
    public CommonResponse<List<SalesReportResponse>> getSalesReport() {
        User user = authenticationService.getLoginUser();
        Merchant merchant = user.getMerchant();

        if (merchant == null) {
            throw new UnauthorizedException("Only merchants can view sales reports.");
        }

        List<Order> orders = orderRepository.findOrdersByMerchantId(merchant.getId());
        List<SalesReportResponse> reports = orders.stream()
                .map(this::toSalesReportResponse)
                .collect(Collectors.toList());

        return CommonResponse.<List<SalesReportResponse>>builder()
                .statusCode(200)
                .message("Sales report fetched successfully")
                .data(reports)
                .build();
    }

    private SalesReportResponse toSalesReportResponse(Order order) {
        return SalesReportResponse.builder()
                .orderId(order.getId())
                .customerName(order.getCustomer().getFullName()) // Tambahkan nama customer
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .status(String.valueOf(order.getStatus()))
                .build();
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .orderDate(order.getOrderDate())
                .status(String.valueOf(order.getStatus()))
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
