package com.majumundur.clothing.dto.response;

import com.majumundur.clothing.entity.enums.SizeChart;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private String id;

    private String productCode;

    private String name;

    private String brand;

    private String size;

    private BigDecimal price;

    private Integer stock;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdate;
}
