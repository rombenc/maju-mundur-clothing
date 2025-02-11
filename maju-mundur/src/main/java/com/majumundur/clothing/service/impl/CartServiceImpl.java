package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.CartRequest;
import com.majumundur.clothing.dto.response.CartResponse;
import com.majumundur.clothing.entity.Cart;
import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Product;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.exception.CartException;
import com.majumundur.clothing.exception.CustomerNotFoundException;
import com.majumundur.clothing.exception.ProductException;
import com.majumundur.clothing.exception.ResourceNotFoundException;
import com.majumundur.clothing.repository.CartRepository;
import com.majumundur.clothing.repository.CustomerRepository;
import com.majumundur.clothing.repository.ProductRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AuthenticationService authenticationService;

    @Override
    public CommonResponse<CartResponse> addToCart(CartRequest request) {
        User user = authenticationService.getLoginUser();

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found", 404));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException("Product not found", 404));

        if (request.getQuantity() > product.getStock()) {
            throw new CartException("Insufficient stock", 400);
        }

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Cart cart = Cart.builder()
                .customer(customer)
                .product(product)
                .quantity(request.getQuantity())
                .price(totalPrice)
                .build();

        Cart savedCart = cartRepository.save(cart);

        return CommonResponse.<CartResponse>builder()
                .statusCode(201)
                .message("Successfully added item to cart")
                .data(toCartResponse(savedCart))
                .build();
    }


    @Override
    public CommonResponse<List<CartResponse>> getCartItems() {
        List<CartResponse> cartItems = cartRepository.findAll().stream()
                .map(this::toCartResponse)
                .toList();

        return CommonResponse.<List<CartResponse>>builder()
                .statusCode(200)
                .message("Successfully retrieved cart items")
                .data(cartItems)
                .build();
    }

    @Override
    public CommonResponse<CartResponse> updateCartItem(String cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException("Cart item not found", 404));

        Product product = cart.getProduct();

        // Cek apakah stok mencukupi
        if (quantity > product.getStock()) {
            throw new CartException("Insufficient stock", 400);
        }

        // Update jumlah dan harga total
        cart.setQuantity(quantity);
        cart.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        Cart updatedCart = cartRepository.save(cart);

        return CommonResponse.<CartResponse>builder()
                .statusCode(200)
                .message("Successfully updated cart item")
                .data(toCartResponse(updatedCart))
                .build();
    }

    @Override
    public CommonResponse<String> removeCartItem(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException("Cart item not found", 404));

        cartRepository.delete(cart);
        return null;
    }

    private CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .productId(cart.getProduct().getId())
                .productName(cart.getProduct().getName())
                .quantity(cart.getQuantity())
                .price(cart.getPrice())
                .build();
    }
}
