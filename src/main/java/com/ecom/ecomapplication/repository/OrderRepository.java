package com.ecom.ecomapplication.repository;

import com.ecom.ecomapplication.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
