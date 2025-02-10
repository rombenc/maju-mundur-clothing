package com.majumundur.clothing.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    private String fullName;

    private LocalDate birthDate;

    private String phoneNumber;

    private String email;

    private BigInteger points;
}
