package com.richard.service;

import com.richard.model.Category;
import com.richard.model.Restaurant;
import com.richard.respository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final RestaurantService restaurantService;
    
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                            RestaurantService restaurantService) {
        this.categoryRepository = categoryRepository;
        this.restaurantService = restaurantService;
    }

    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(userId);
        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }
    
    @Override
    public List<Category> findCategoriesByRestaurantId(Long restaurantId) throws Exception {
        return categoryRepository.findCategoriesByRestaurantId(restaurantId);
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
