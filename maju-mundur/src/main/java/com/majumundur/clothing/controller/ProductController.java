package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.ProductRequest;
import com.majumundur.clothing.dto.response.ProductResponse;
import com.majumundur.clothing.entity.Product;
import com.majumundur.clothing.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product Management")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('MERCHANT')")
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest payload) {

        CommonResponse<ProductResponse> response = productService.createProduct(payload);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProducts() {
        CommonResponse<List<ProductResponse>> response = productService.getAllProducts();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getProductByName(@RequestParam String name) {
        CommonResponse<List<ProductResponse>> response = productService.getProductByName(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/brands")
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getProductByBrand(@RequestParam String brand) {
        var response = productService.getProductByBrand(brand);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest payload) {
        CommonResponse<ProductResponse> response = productService.updateProduct(id, payload);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteProduct(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.ok(CommonResponse.<String>builder()
                .statusCode(200)
                .message("Successfully deleted product with id: " + id)
                .build());
    }
}

