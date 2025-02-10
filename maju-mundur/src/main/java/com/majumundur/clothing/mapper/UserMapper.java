package com.majumundur.clothing.mapper;

import com.majumundur.clothing.dto.response.RegisterResponse;
import com.majumundur.clothing.dto.response.UpdateNameResponse;
import com.majumundur.clothing.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public RegisterResponse RegisterMapper(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .createdDate(user.getCreatedDate())
                .verificationCode(user.getVerificationCode())
                .roles(user.getRoles())
                .build();
    }

    public UpdateNameResponse updateNameMapper(User user) {
        return UpdateNameResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .roles(user.getRoles())
                .build();
    }
}
