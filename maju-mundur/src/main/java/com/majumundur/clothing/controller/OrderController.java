package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.OrderRequest;
import com.majumundur.clothing.dto.response.OrderHistoryResponse;
import com.majumundur.clothing.dto.response.OrderResponse;
import com.majumundur.clothing.dto.response.SalesReportResponse;
import com.majumundur.clothing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(@RequestBody OrderRequest request) {
        CommonResponse<OrderResponse> response = orderService.createOrder(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getOrders() {
        CommonResponse<List<OrderResponse>> response = orderService.getOrdersByCustomer();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> getOrderById(@PathVariable String orderId) {
        CommonResponse<OrderResponse> response = orderService.getOrderById(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        CommonResponse<OrderResponse> response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
