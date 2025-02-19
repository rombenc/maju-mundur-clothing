package com.majumundur.clothing.dto.response;

import com.majumundur.clothing.entity.Address;
import com.majumundur.clothing.entity.User;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CustomerResponse {
    private String id;

    private User user;

    private String fullName;

    private LocalDate birthDate;

    private String phoneNumber;

    private String email;

    private BigInteger points;

    private List<Address> addresses;
}
