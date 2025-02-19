package com.majumundur.clothing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.majumundur.clothing.entity.enums.SizeChart;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    private String name;
    private String brand;
    @Enumerated(EnumType.STRING)
    private SizeChart size;
    private BigDecimal price;
    private Integer stock;
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

}
