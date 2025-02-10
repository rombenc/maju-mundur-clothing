package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.CustomerRequest;
import com.majumundur.clothing.dto.response.CustomerResponse;
import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.exception.ResourceNotFoundException;
import com.majumundur.clothing.exception.UnauthorizedException;
import com.majumundur.clothing.repository.CustomerRepository;
import com.majumundur.clothing.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final AuthenticationServiceImpl authenticationService;

    @Override
    public CommonResponse<CustomerResponse> createCustomer(CustomerRequest request){
        User user = authenticationService.getLoginUser();

        boolean isCustomerRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("CUSTOMER"));

        if (!isCustomerRole) {
            log.error("Unauthorized attempt to create customer by user with id: {}", user.getId());
            throw new UnauthorizedException("Only users with role CUSTOMER can create a customer account.");
        }

        Customer customer = Customer.builder()
                .user(user)
                .fullName(user.getFullName())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
        Customer savedCustomer = repository.save(customer);

        user.setCustomer(savedCustomer);

        return CommonResponse.<CustomerResponse>builder()
                .statusCode(201)
                .message("Successfully created customer account")
                .data(toCustomerResponse(savedCustomer))
                .build();
    }


    @Override
    public CommonResponse<List<CustomerResponse>> getAllCustomer() {
        List<CustomerResponse> customerResponses = repository.findAll()
                .stream()
                .map(this::toCustomerResponse)
                .collect(Collectors.toList());

        return CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(200)
                .message("Successfully retrieved all customers")
                .data(customerResponses)
                .build();
    }

    @Override
    public CommonResponse<CustomerResponse> updateCustomer(String customerId, CustomerRequest request) {
        User user = authenticationService.getLoginUser();

        boolean isCustomerRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("CUSTOMER"));

        if (!isCustomerRole) {
            log.error("Unauthorized attempt to update customer by user with id: {}", user.getId());
            throw new UnauthorizedException("Only users with role CUSTOMER can update a customer account.");
        }

        Customer customer = repository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        customer.setBirthDate(request.getBirthDate());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());

        Customer updatedCustomer = repository.save(customer);

        return CommonResponse.<CustomerResponse>builder()
                .statusCode(200)
                .message("Successfully updated customer account")
                .data(toCustomerResponse(updatedCustomer))
                .build();
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .birthDate(customer.getBirthDate())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .points(customer.getPoints())
                .build();
    }
}
