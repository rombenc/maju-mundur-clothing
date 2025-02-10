package com.majumundur.clothing.service.impl;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.MerchantRequest;
import com.majumundur.clothing.dto.response.MerchantResponse;
import com.majumundur.clothing.entity.Merchant;
import com.majumundur.clothing.entity.User;
import com.majumundur.clothing.exception.ResourceNotFoundException;
import com.majumundur.clothing.exception.UnauthorizedException;
import com.majumundur.clothing.repository.MerchantRepository;
import com.majumundur.clothing.service.AuthenticationService;
import com.majumundur.clothing.service.MerchantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final AuthenticationService authenticationService;

    @Override
    @Transactional
    public CommonResponse<MerchantResponse> create(MerchantRequest request){

        User user = authenticationService.getLoginUser();

        boolean isCustomerRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("MERCHANT"));

        if (!isCustomerRole) {
            throw new UnauthorizedException("Only users with role MERCHANT can create a merchant account.");
        }
        var merchant = Merchant.builder()
                .user(user)
                .fullName(user.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .createdAt(LocalDate.now())
                .build();
        var savedMerchant = merchantRepository.save(merchant);
        return CommonResponse.<MerchantResponse>builder()
                .statusCode(201)
                .message("Merchant has been created!")
                .data(toMerchantResponse(savedMerchant))
                .build();
    }
    @Override
    public CommonResponse<List<MerchantResponse>> getAllMerchants() {
        List<MerchantResponse> merchantResponses = merchantRepository.findAll()
                .stream()
                .map(this::toMerchantResponse)
                .collect(Collectors.toList());

        return CommonResponse.<List<MerchantResponse>>builder()
                .statusCode(200)
                .message("Successfully retrieved all merchants")
                .data(merchantResponses)
                .build();
    }
    @Transactional
    @Override
    public CommonResponse<MerchantResponse> updateMerchant(String merchantId, MerchantRequest request) {
        User user = authenticationService.getLoginUser();

        boolean isMerchantRole = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("MERCHANT"));

        if (!isMerchantRole) {
            log.error("Unauthorized attempt to update merchant by user with id: {}", user.getId());
            throw new UnauthorizedException("Only users with role MERCHANT can update a merchant account.");
        }

        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + merchantId));

        merchant.setPhoneNumber(request.getPhoneNumber());
        merchant.setEmail(request.getEmail());

        Merchant updatedMerchant = merchantRepository.save(merchant);

        return CommonResponse.<MerchantResponse>builder()
                .statusCode(200)
                .message("Successfully updated merchant account")
                .data(toMerchantResponse(updatedMerchant))
                .build();
    }

    private MerchantResponse toMerchantResponse(Merchant merchant) {
        return MerchantResponse.builder()
                .id(merchant.getId())
                .fullName(merchant.getFullName())
                .phoneNumber(merchant.getPhoneNumber())
                .email(merchant.getEmail())
                .createdAt(merchant.getCreatedAt())
                .build();
    }
}
