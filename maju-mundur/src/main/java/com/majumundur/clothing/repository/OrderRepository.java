package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomer(Customer customer);
}
