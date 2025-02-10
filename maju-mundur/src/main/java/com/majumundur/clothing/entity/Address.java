package com.majumundur.clothing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String country;
    private String city;
    private String street;
    private BigInteger postalCode;
    private String detail;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
