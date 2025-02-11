package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Customer;
import com.majumundur.clothing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByUser(User user);
}
