package com.majumundur.clothing.dto.response;

import com.majumundur.clothing.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String fullName;
    private String email;
    private String password;
    private boolean verified;
    private List<Role> roles;
    private String verificationCode;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
