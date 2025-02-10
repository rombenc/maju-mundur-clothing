package com.majumundur.clothing.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private String customerId;
    private String productIds;
}
