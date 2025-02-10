package com.majumundur.clothing.dto.request;

import com.majumundur.clothing.entity.enums.SizeChart;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {

    private String productCode;

    private String name;

    private String brand;

    private String size;

    private BigDecimal price;

    private Integer stock;

    private String description;

}
