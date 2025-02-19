package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomer(Customer customer);
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.carts c " +
            "WHERE c.product.merchant.id = :merchantId")
    List<Order> findOrdersByMerchantId(@Param("merchantId") String merchantId);

    List<Order> findOrdersByCustomerId(String id);
}
