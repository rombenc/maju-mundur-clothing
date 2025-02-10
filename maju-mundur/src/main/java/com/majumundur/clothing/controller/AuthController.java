package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.LoginRequest;
import com.majumundur.clothing.dto.request.RegisterRequest;
import com.majumundur.clothing.dto.request.UpdateNameRequest;
import com.majumundur.clothing.dto.response.LoginResponse;
import com.majumundur.clothing.dto.response.RegisterResponse;
import com.majumundur.clothing.dto.response.UpdateNameResponse;
import com.majumundur.clothing.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
@Tag(name = "Authentication", description = "Authentication Management")
public class AuthController {
    private final AuthenticationService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCustomer(@Valid @RequestBody RegisterRequest request) {
        CommonResponse<RegisterResponse> register = authService.registerCustomer(request);
        return ResponseEntity.status(register.getStatusCode()).body(register);
    }

    @PostMapping("/register/merchant")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerMerchant(@Valid @RequestBody RegisterRequest request) {
        CommonResponse<RegisterResponse> register = authService.registerMerchant(request);
        return ResponseEntity.status(register.getStatusCode()).body(register);
    }

    @PostMapping("/verify")
    public ResponseEntity<CommonResponse<String>> verifyUser(@RequestParam String email, @RequestParam String code) {
        return ResponseEntity.ok(authService.verifyUser(email, code));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        CommonResponse<LoginResponse> response = authService.login(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-email")
    public ResponseEntity<CommonResponse<Void>> updateEmail(@RequestParam String currentPassword, @RequestParam String newEmail) {
        CommonResponse<Void> response = authService.updateEmail(currentPassword, newEmail);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-name")
    public ResponseEntity<CommonResponse<UpdateNameResponse>> updateFirstAndLast(@RequestParam String currentPassword, @Valid @RequestBody UpdateNameRequest request) {
        CommonResponse<UpdateNameResponse> response = authService.updateFirstAndLast(currentPassword, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

        @PutMapping("/update-password")
    public ResponseEntity<CommonResponse<Void>> updatePassword(@RequestParam String currentPassword, @RequestParam String newPassword) {
        CommonResponse<Void> response = authService.updatePassword(currentPassword, newPassword);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable String userId) {
        CommonResponse<Void> response = authService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}