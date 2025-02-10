package com.majumundur.clothing.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    private String productId;
    private int quantity;
}
