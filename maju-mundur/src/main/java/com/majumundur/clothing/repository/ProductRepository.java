package com.majumundur.clothing.repository;

import com.majumundur.clothing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.name ASC")
    List<Product> findAllByNameLikeIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%')) ORDER BY p.brand ASC")
    List<Product> findAllByBrandLikeIgnoreCase(@Param("brand") String brand);
}
