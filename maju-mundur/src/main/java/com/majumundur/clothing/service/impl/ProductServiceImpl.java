package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.ProductRequest;
import com.majumundur.clothing.dto.response.ProductResponse;
import com.majumundur.clothing.entity.Product;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.entity.enums.SizeChart;
import com.majumundur.clothing.exception.ProductException;
import com.majumundur.clothing.exception.UnauthorizedException;
import com.majumundur.clothing.repository.ProductRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;

    @Override
    public CommonResponse<Object> createProduct(String userMerchantId, ProductRequest payload) {

        User user = authenticationService.getLoginUser();

        boolean isCustomerRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("MERCHANT"));

        if (!isCustomerRole) {
            throw new UnauthorizedException("Only users with role MERCHANT can create a merchant account.");
        }

        if (payload.getProductCode() == null || payload.getProductCode().isEmpty()) {
            throw new ProductException("Product code is required", 400);
        }

        var product = Product.builder()
                .productCode(payload.getProductCode())
                .name(payload.getName())
                .brand(payload.getBrand())
                .size(SizeChart.valueOf(payload.getSize()))
                .price(payload.getPrice())
                .stock(payload.getStock())
                .description(payload.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        var savedProduct = productRepository.save(product);

        return CommonResponse.builder()
                .statusCode(200)
                .message("Successfully created a product")
                .data(savedProduct)
                .build();
    }


    @Override
    public CommonResponse<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return CommonResponse.<List<Product>>builder()
                .statusCode(200)
                .message("Successfully fetched all products")
                .data(products)
                .build();
    }

    @Override
    public CommonResponse<List<ProductResponse>> getProductByName(String name) {
        List<ProductResponse> products;

        if (name != null && !name.isEmpty()) {
            String[] keywords = name.toLowerCase().split("\\s+");
            String searchPattern = "%" + String.join("%", keywords) + "%";

            products = productRepository.findAllByNameLikeIgnoreCase(searchPattern)
                    .stream()
                    .map(this::toProductResponse)
                    .toList();
        } else {
            products = productRepository.findAll()
                    .stream()
                    .map(this::toProductResponse)
                    .toList();
        }

        return CommonResponse.<List<ProductResponse>>builder()
                .statusCode(200)
                .message("Successfully fetched products")
                .data(products)
                .build();
    }

    @Override
    public CommonResponse<List<ProductResponse>> getProductByBrand(String brand) {
        List<ProductResponse> products;

        if (brand != null && !brand.isEmpty()) {
            String[] keywords = brand.toLowerCase().split("\\s+");
            String searchPattern = "%" + String.join("%", keywords) + "%";

            products = productRepository.findAllByBrandLikeIgnoreCase(searchPattern)
                    .stream()
                    .map(this::toProductResponse)
                    .toList();
        } else {
            products = productRepository.findAll()
                    .stream()
                    .map(this::toProductResponse)
                    .toList();
        }

        return CommonResponse.<List<ProductResponse>>builder()
                .statusCode(200)
                .message("Successfully fetched products by brand")
                .data(products)
                .build();
    }


    @Override
    public CommonResponse<ProductResponse> update(String id, ProductRequest payload) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product not found", 404));

        product.setProductCode(payload.getProductCode());
        product.setBrand(payload.getBrand());
        product.setPrice(payload.getPrice());
        product.setStock(payload.getStock());
        product.setDescription(payload.getDescription());
        product.setLastUpdate(LocalDateTime.now());

        var updatedProduct = productRepository.save(product);

        return CommonResponse.<ProductResponse>builder()
                .statusCode(200)
                .message("Successfully updated the product")
                .data(toProductResponse(updatedProduct))
                .build();
    }

    @Override
    public void delete(String id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product with given id: " + id + " can't be found", 404));

        productRepository.delete(product);
    }


    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .name(product.getName())
                .brand(product.getBrand())
                .size(String.valueOf(product.getSize()))
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .lastUpdate(product.getLastUpdate())
                .build();
    }
}
