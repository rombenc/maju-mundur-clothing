package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
}
