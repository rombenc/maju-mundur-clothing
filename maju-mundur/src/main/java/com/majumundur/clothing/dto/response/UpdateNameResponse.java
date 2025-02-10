package com.majumundur.clothing.dto.response;

import com.majumundur.clothing.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UpdateNameResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String fullName;
    private String email;
    private List<Role> roles;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
