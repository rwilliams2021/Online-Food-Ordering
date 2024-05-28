package com.richard.service;

import com.richard.model.Category;
import com.richard.model.Restaurant;
import com.richard.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }
    
    @Override
    public List<Category> findCategoriesByRestaurantId(Long userId) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        return categoryRepository.findCategoriesByRestaurantId(restaurant.getId());
    }
    
    @Override
    public Category findCategoryById(Long id) throws Exception {
        return categoryRepository.findById(id).orElseThrow(() -> new Exception("Category not found"));
    }
    
    @Override
    public void deleteCategoryById(Long id) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
