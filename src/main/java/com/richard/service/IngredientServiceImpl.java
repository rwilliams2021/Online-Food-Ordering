package com.richard.service;

import com.richard.model.IngredientCategory;
import com.richard.model.IngredientItem;
import com.richard.model.Restaurant;
import com.richard.respository.IngredientCategoryRepository;
import com.richard.respository.IngredientItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final IngredientItemRepository ingredientItemRepository;
    private final RestaurantService restaurantService;
    
    public IngredientServiceImpl(IngredientCategoryRepository ingredientCategoryRepository,
                                 IngredientItemRepository ingredientItemRepository,
                                 RestaurantService restaurantService) {
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.ingredientItemRepository = ingredientItemRepository;
        this.restaurantService = restaurantService;
    }
    
    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = new IngredientCategory();
        category.setName(name);
        category.setRestaurant(restaurant);
        return ingredientCategoryRepository.save(category);
    }
    
    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
        return ingredientCategoryRepository.findById(id).orElseThrow(() -> new Exception("Ingredient Category not found"));
    }
    
    @Override
    public List<IngredientCategory> findIngredientCategoriesByRestaurantId(Long restaurantId) throws Exception {
        restaurantService.findRestaurantById(restaurantId);
        return ingredientCategoryRepository.findByRestaurantId(restaurantId);
    }
    
    @Override
    public IngredientItem createIngredientItem(String ingredientName, Long ingredientCategoryId, Long restaurantId) throws
                                                                                                                    Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        IngredientCategory category = findIngredientCategoryById(ingredientCategoryId);
        IngredientItem item = new IngredientItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);
        category.getIngredients().add(item);
        return ingredientItemRepository.save(item);
    }
    
    @Override
    public List<IngredientItem> findRestaurantIngredients(Long restaurantId) {
        return ingredientItemRepository.findByRestaurantId(restaurantId);
    }
    
    @Override
    public IngredientItem updateStock(Long id) throws Exception {
        IngredientItem ingredient =
                ingredientItemRepository.findById(id).orElseThrow(() -> new Exception("Ingredient not found"));
        ingredient.setInStock(!ingredient.isInStock());
        return ingredientItemRepository.save(ingredient);
    }
}
