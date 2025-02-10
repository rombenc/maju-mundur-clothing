package com.majumundur.clothing.dto.response;

import com.majumundur.clothing.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MerchantResponse {
    private String id;

    private User user;

    private String fullName;

    private String phoneNumber;

    private String email;

    private LocalDate createdAt;
}
