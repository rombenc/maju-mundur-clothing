package com.majumundur.clothing.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String firstname;

    private String lastname;

    @Column(unique = true, nullable = false)
    @Email(message = "enter the valid email sellerAddresses!")
    private String email;

    private String password;

}
