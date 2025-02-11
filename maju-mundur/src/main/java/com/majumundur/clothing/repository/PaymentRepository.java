package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Order;
import com.majumundur.clothing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByOrder_Customer(Customer customer);
    Optional<Payment> findByOrder(Order order);
}
