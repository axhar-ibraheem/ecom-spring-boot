package com.ecom.ecomapplication.repository;

import com.ecom.ecomapplication.model.CartItem;
import com.ecom.ecomapplication.model.Product;
import com.ecom.ecomapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserAndProduct(User user, Product product);
}
