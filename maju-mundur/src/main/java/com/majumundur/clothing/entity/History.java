package com.majumundur.clothing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "histories")
public class History {
    @Id
    @GeneratedValue
    private String id;

    private LocalDateTime transactionDate;

    @ManyToOne
    private Customer customer;

}
