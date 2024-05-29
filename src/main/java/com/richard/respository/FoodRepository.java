package com.richard.respository;

import com.richard.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    
    List<Food> findByRestaurantId(Long id);
    @Query("SELECT f FROM Food f WHERE lower(f.name) LIKE %:keyword% OR f.foodCategory.name LIKE %:keyword%")
    List<Food> searchFood(String keyword);
}
