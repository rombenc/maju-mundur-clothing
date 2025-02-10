package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String username);
    boolean existsByEmail(@Email(message = "enter the valid email sellerAddresses!") String email);

}
