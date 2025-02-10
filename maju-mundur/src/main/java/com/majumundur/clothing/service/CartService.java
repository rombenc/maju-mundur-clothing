package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.CartRequest;
import com.majumundur.clothing.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    CommonResponse<CartResponse> addToCart(CartRequest request);
    CommonResponse<List<CartResponse>> getCartItems();
    CommonResponse<CartResponse> updateCartItem(String cartId, int quantity);
    CommonResponse<String> removeCartItem(String cartId);
}
