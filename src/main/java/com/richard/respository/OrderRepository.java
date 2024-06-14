package com.richard.respository;

import com.richard.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(Long userId);
    List<Order> findAllByRestaurantId(Long restaurantId);
    void deleteAllByCustomerId(Long userId);
}
