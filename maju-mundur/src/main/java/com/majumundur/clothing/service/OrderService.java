package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.OrderRequest;
import com.majumundur.clothing.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    CommonResponse<OrderResponse> createOrder(OrderRequest request);

    CommonResponse<List<OrderResponse>> getOrdersByCustomer();

    CommonResponse<OrderResponse> getOrderById(String orderId);

    CommonResponse<OrderResponse> updateOrderStatus(String orderId, String status);
}
