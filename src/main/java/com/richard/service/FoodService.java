package com.richard.service;

import com.richard.model.Category;
import com.richard.model.Food;
import com.richard.model.Restaurant;
import com.richard.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);
    void deleteFood(Long id);
    List<Food> getRestaurantFood(Long id, boolean isVegetarian, boolean isNonVegetarian, boolean isSeasonal, String category);
    List<Food> searchFood(String keyword);
    Food findFoodById(Long id) throws Exception;
    Food updateAvailability(Long id) throws Exception;
}
