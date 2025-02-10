package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.ProductRequest;
import com.majumundur.clothing.dto.response.ProductResponse;
import com.majumundur.clothing.entity.Product;

import java.util.List;

public interface ProductService {

    CommonResponse<Object> createProduct(String userMerchantId, ProductRequest payload);

    CommonResponse<List<Product>> getAllProducts();

    CommonResponse<List<ProductResponse>> getProductByName(String name);

    CommonResponse<List<ProductResponse>> getProductByBrand(String brand);

    CommonResponse<ProductResponse> update(String id, ProductRequest payload);

    void delete(String id);
}
