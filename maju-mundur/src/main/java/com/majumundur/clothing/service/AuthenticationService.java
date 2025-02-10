package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.LoginRequest;
import com.majumundur.clothing.dto.request.RegisterRequest;
import com.majumundur.clothing.dto.request.UpdateNameRequest;
import com.majumundur.clothing.dto.response.LoginResponse;
import com.majumundur.clothing.dto.response.RegisterResponse;
import com.majumundur.clothing.dto.response.UpdateNameResponse;
import com.majumundur.clothing.entity.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface AuthenticationService {
    @Transactional
    CommonResponse<RegisterResponse> registerCustomer(@Valid RegisterRequest request);

    @Transactional
    CommonResponse<RegisterResponse> registerMerchant(@Valid RegisterRequest request);

    CommonResponse<String> verifyUser(String email, String verificationCode);

    @Transactional
    CommonResponse<LoginResponse> login(@Valid LoginRequest request);

    @Transactional
    CommonResponse<Void> updateEmail(String currentPassword, String newEmail);

    @Transactional
    CommonResponse<UpdateNameResponse> updateFirstAndLast(String currentPassword, UpdateNameRequest request);

    @Transactional
    CommonResponse<Void> updatePassword(String currentPassword, String newPassword);

    @Transactional
    CommonResponse<Void> deleteUser(String userId);

    User getLoginUser();
}
