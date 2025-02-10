package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.LoginRequest;
import com.majumundur.clothing.dto.request.RegisterRequest;
import com.majumundur.clothing.dto.request.UpdateNameRequest;
import com.majumundur.clothing.dto.response.LoginResponse;
import com.majumundur.clothing.dto.response.RegisterResponse;
import com.majumundur.clothing.dto.response.UpdateNameResponse;
import com.majumundur.clothing.email.EmailService;
import com.majumundur.clothing.entity.Role;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.exception.ResourceNotFoundException;
import com.majumundur.clothing.exception.UnverifiedUserException;
import com.majumundur.clothing.exception.UserAlreadyExistsException;
import com.majumundur.clothing.exception.UserException;
import com.majumundur.clothing.mapper.UserMapper;
import com.majumundur.clothing.repository.RoleRepository;
import com.majumundur.clothing.repository.UserRepository;
import com.majumundur.clothing.security.JwtUtils;
import com.majumundur.clothing.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public CommonResponse<RegisterResponse> registerCustomer(@Valid RegisterRequest request) {
        log.info("Starting user registration for email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered", 400);
        }
        var userRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new UserException("ROLE CUSTOMER was not initiated", 500));

        return register(request, userRole);
    }

    @Transactional
    @Override
    public CommonResponse<RegisterResponse> registerMerchant(@Valid RegisterRequest request) {
        log.info("Starting MERCHANT registration for email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered", 400);
        }
        var userRole = roleRepository.findByName("MERCHANT")
                .orElseThrow(() -> new UserException("ROLE MERCHANT was not initiated", 500));

        return register(request, userRole);
    }

    @Override
    public CommonResponse<String> verifyUser(String email, String verificationCode) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            if (user.getVerificationCode().equals(verificationCode)) {
                user.setVerified(true);
                user.setVerificationCode(null);
                userRepository.save(user);
                return new CommonResponse<>(200, "Email verified successfully", "Verified");
            } else {
                return new CommonResponse<>(400, "Invalid verification code", null);
            }
        } catch (Exception e) {
            return CommonResponse.<String>builder()
                    .statusCode(403)
                    .message("Invalid verification code!")
                    .build();
        }
    }

    @Transactional
    @Override
    public CommonResponse<LoginResponse> login(@Valid LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException("Invalid email or password", 400));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException("Invalid email or password", 400);
        }

        if (!user.isVerified()) {
            throw new UnverifiedUserException("Please continue your verification", 400);
        }

        String token = jwtUtil.generateToken(user);

        return CommonResponse.<LoginResponse>builder()
                .statusCode(200)
                .message("Login successful")
                .data(LoginResponse.builder()
                        .id(user.getId())
                        .token(token)
                        .build())
                .build();
    }

    @Transactional
    @Override
    public CommonResponse<Void> updateEmail(String currentPassword, String newEmail) {
        User user = getLoginUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException("Current password is incorrect", 400);
        }
        if (userRepository.existsByEmail(newEmail)) {
            throw new UserAlreadyExistsException("Email is already registered", 400);
        }
        user.setEmail(newEmail);
        userRepository.save(user);

        return CommonResponse.<Void>builder()
                .statusCode(200)
                .message("Email updated successfully")
                .build();
    }

    @Transactional
    @Override
    public CommonResponse<UpdateNameResponse> updateFirstAndLast(String currentPassword, UpdateNameRequest request) {
        User user = getLoginUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException("Current password is incorrect", 400);
        }
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setLastModifiedDate(LocalDateTime.now());
        userRepository.save(user);

        return CommonResponse.<UpdateNameResponse>builder()
                .statusCode(200)
                .message("successfully updating your name")
                .data(userMapper.updateNameMapper(user))
                .build();
    }


    @Transactional
    @Override
    public CommonResponse<Void> updatePassword(String currentPassword, String newPassword) {
        User user = getLoginUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserException("Current password is incorrect", 400);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return CommonResponse.<Void>builder()
                .statusCode(200)
                .message("Password updated successfully")
                .build();
    }

    @Transactional
    @Override
    public CommonResponse<Void> deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);

        return CommonResponse.<Void>builder()
                .statusCode(200)
                .message("Successfully deleted user and related entities")
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();
        log.info("User Email is: " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));
    }

    private CommonResponse<RegisterResponse> register(@Valid RegisterRequest request, Role userRole) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(hashedPassword)
                .verificationCode(generateVerificationCode())
                .createdDate(LocalDateTime.now())
                .roles(List.of(userRole))
                .build();
        try {
            var saved = userRepository.save(user);
            sendVerificationEmail(saved);
            return CommonResponse.<RegisterResponse>builder()
                    .statusCode(200)
                    .message("Please verify your email")
                    .data(userMapper.RegisterMapper(saved))
                    .build();
        } catch (Exception e) {
            log.error("Failed to send verification email", e);
            throw new UserException("Failed to send verification email", 500);
        }
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, verificationCode);
            log.info("Verification email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
