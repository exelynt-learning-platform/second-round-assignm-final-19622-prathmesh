package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUser(User user);
}
