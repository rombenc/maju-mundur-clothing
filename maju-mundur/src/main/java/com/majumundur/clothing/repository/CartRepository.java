package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Cart;
import com.majumundur.clothing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query("SELECT c FROM Cart c WHERE c.customer = :customer AND c.order IS NULL")
    List<Cart> findByCustomerAndOrderIsNull(@Param("customer") Customer customer);
}
