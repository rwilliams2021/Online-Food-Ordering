package com.richard.service;

import com.richard.model.IngredientCategory;
import com.richard.model.IngredientItem;

import java.util.List;

public interface IngredientService {
    IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception;
    IngredientCategory findIngredientCategoryById(Long id) throws Exception;
    List<IngredientCategory> findIngredientCategoriesByRestaurantId(Long restaurantId) throws Exception;
    IngredientItem createIngredientItem(String ingredientName, Long ingredientCategoryId, Long restaurantId) throws Exception;
    List<IngredientItem> findRestaurantIngredients(Long restaurantId);
    IngredientItem updateStock(Long itemId) throws Exception;
}
