package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.CartRequest;
import com.majumundur.clothing.dto.response.CartResponse;
import com.majumundur.clothing.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CommonResponse<CartResponse>> addToCart(@RequestBody CartRequest request) {
        CommonResponse<CartResponse> response = cartService.addToCart(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping
    public ResponseEntity<CommonResponse<List<CartResponse>>> getCartItems() {
        CommonResponse<List<CartResponse>> response = cartService.getCartItems();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CommonResponse<CartResponse>> updateCartItem(@PathVariable String cartId, @RequestParam int quantity) {
        CommonResponse<CartResponse> response = cartService.updateCartItem(cartId, quantity);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<CommonResponse<String>> removeCartItem(@PathVariable String cartId) {
        CommonResponse<String> response = cartService.removeCartItem(cartId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
