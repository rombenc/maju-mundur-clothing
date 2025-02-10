package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.CustomerRequest;
import com.majumundur.clothing.dto.response.CustomerResponse;
import com.majumundur.clothing.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> createCustomer(@RequestBody CustomerRequest request) {
        CommonResponse<CustomerResponse> response = customerService.createCustomer(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers() {
        CommonResponse<List<CustomerResponse>> response = customerService.getAllCustomer();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@PathVariable String customerId, @RequestBody CustomerRequest request) {
        CommonResponse<CustomerResponse> response = customerService.updateCustomer(customerId, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
