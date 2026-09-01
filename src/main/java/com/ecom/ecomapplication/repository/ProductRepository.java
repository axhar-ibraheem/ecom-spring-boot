package com.ecom.ecomapplication.repository;

import com.ecom.ecomapplication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("""
                SELECT p FROM Product p
                WHERE p.active = true
                AND p.stockQuantity > 0
                AND (
                    LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    List<Product> searchActiveProducts(@Param("keyword") String keyword);
}
