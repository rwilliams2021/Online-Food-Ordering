package com.richard.respository;

import com.richard.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    List<Category> findCategoriesByRestaurantId(Long restaurantId);
}
