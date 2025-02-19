package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.ProductRequest;
import com.majumundur.clothing.dto.response.ProductResponse;
import com.majumundur.clothing.entity.Merchant;
import com.majumundur.clothing.entity.Product;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.entity.enums.SizeChart;
import com.majumundur.clothing.exception.ProductException;
import com.majumundur.clothing.exception.ResourceNotFoundException;
import com.majumundur.clothing.exception.UnauthorizedException;
import com.majumundur.clothing.repository.ProductRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    @Override
    public CommonResponse<ProductResponse> createProduct(ProductRequest request) {
        User user = authenticationService.getLoginUser();
        Merchant merchant = user.getMerchant();

        var product = Product.builder()
                .productCode(request.getProductCode())
                .name(request.getName())
                .brand(request.getBrand())
                .size(SizeChart.valueOf(request.getSize()))
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .merchant(merchant)
                .createdAt(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        return CommonResponse.<ProductResponse>builder()
                .statusCode(201)
                .message("Product has been created")
                .data(toProductResponse(savedProduct))
                .build();
    }

    @Transactional
    @Override
    public CommonResponse<ProductResponse> updateProduct(String productId, ProductRequest request) {
        User user = authenticationService.getLoginUser();
        Merchant merchant = user.getMerchant();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!product.getMerchant().equals(merchant)) {
            throw new UnauthorizedException("You are not authorized to update this product");
        }

        product.setProductCode(request.getProductCode());
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setSize(SizeChart.valueOf(request.getSize()));
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setLastUpdate(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        return CommonResponse.<ProductResponse>builder()
                .statusCode(200)
                .message("Successfully updated the product")
                .data(toProductResponse(updatedProduct))
                .build();
    }

    @Override
    public CommonResponse<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return CommonResponse.<List<ProductResponse>>builder()
                .statusCode(200)
                .message("Successfully retrieved all products")
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
    public void delete(String id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("Product with given id: " + id + " can't be found", 404));

        productRepository.delete(product);
    }


    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .merchantId(String.valueOf(product.getMerchant().getId()))
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
