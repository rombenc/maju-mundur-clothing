package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.CustomerRequest;
import com.majumundur.clothing.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CommonResponse<CustomerResponse> createCustomer (CustomerRequest request);

    CommonResponse<List<CustomerResponse>> getAllCustomer();

    CommonResponse<CustomerResponse> updateCustomer(String customerId, CustomerRequest request);
}
