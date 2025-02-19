package com.majumundur.clothing.controller;

import com.majumundur.clothing.dto.CommonResponse;
import com.majumundur.clothing.dto.request.MerchantRequest;
import com.majumundur.clothing.dto.response.MerchantResponse;
import com.majumundur.clothing.dto.response.SalesReportResponse;
import com.majumundur.clothing.service.MerchantService;
import com.majumundur.clothing.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CommonResponse<MerchantResponse>> createMerchant(@RequestBody MerchantRequest request) {
        CommonResponse<MerchantResponse> response = merchantService.create(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<MerchantResponse>>> getAllMerchants() {
        CommonResponse<List<MerchantResponse>> response = merchantService.getAllMerchants();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{merchantId}")
    public ResponseEntity<CommonResponse<MerchantResponse>> updateMerchant(@PathVariable String merchantId, @RequestBody MerchantRequest request) {
        CommonResponse<MerchantResponse> response = merchantService.updateMerchant(merchantId, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/sales-report")
    @PreAuthorize("hasAuthority('MERCHANT')") // ✅ Hanya merchant yang bisa mengakses
    public ResponseEntity<CommonResponse<List<SalesReportResponse>>> getSalesReport() {
        CommonResponse<List<SalesReportResponse>> response = orderService.getSalesReport();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

