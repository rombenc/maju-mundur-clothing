package com.majumundur.clothing.service;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.MerchantRequest;
import com.majumundur.clothing.dto.response.MerchantResponse;

import java.util.List;

public interface MerchantService {
    CommonResponse<MerchantResponse> create (MerchantRequest request);

    CommonResponse<List<MerchantResponse>> getAllMerchants();

    CommonResponse<MerchantResponse> updateMerchant(String merchantId, MerchantRequest request);
}
