package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.ProductRequest;
import com.majumundur.clothing.dto.response.ProductResponse;
import com.majumundur.clothing.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {

    @Transactional
    CommonResponse<ProductResponse> createProduct(ProductRequest request);

    @Transactional
    CommonResponse<ProductResponse> updateProduct(String productId, ProductRequest request);

    CommonResponse<List<ProductResponse>> getAllProducts();

    CommonResponse<List<ProductResponse>> getProductByName(String name);

    CommonResponse<List<ProductResponse>> getProductByBrand(String brand);

    void delete(String id);
}
