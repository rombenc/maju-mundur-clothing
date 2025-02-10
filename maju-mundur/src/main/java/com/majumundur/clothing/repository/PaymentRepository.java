package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByOrder_Customer(Customer customer);
}
