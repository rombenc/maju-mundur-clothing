package com.majumundur.clothing.dto.request;

import com.majumundur.clothing.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MerchantRequest {

        private String fullName;

        private String phoneNumber;

        private String email;

        private LocalDate createdAt;
}
