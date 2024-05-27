package com.richard.service;

import com.richard.model.Category;
import com.richard.model.Food;
import com.richard.model.Restaurant;
import com.richard.request.CreateFoodRequest;
import com.richard.respository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;
    
    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredients(req.getIngredients());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());
        
        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);
        
        return savedFood;
    }
    
    @Override
    public void deleteFood(Long id) throws Exception {
        Food food = findFoodById(id);
        food.setRestaurant(null);
        foodRepository.save(food);
    }
    
    @Override
    public List<Food> getRestaurantFood(Long id,
                                        boolean isVegetarian,
                                        boolean isNonVegetarian,
                                        boolean isSeasonal,
                                        String category) {
        List<Food> foods = foodRepository.findByRestaurantId(id);
        if (isVegetarian) {
            foods = foods.stream().filter(Food::isVegetarian).toList();
        }
        if (isNonVegetarian) {
            foods = foods.stream().filter(Food::isNonVegetarian).toList();
        }
        if (isSeasonal) {
            foods = foods.stream().filter(Food::isSeasonal).toList();
        }
        if (category != null && !category.isEmpty()) {
            foods = filterByCategory(foods, category);
        }
        return foods;
    }
    
    private List<Food> filterByCategory(List<Food> foods, String category) {
        return foods.stream().filter(food -> {
            if (food.getFoodCategory() == null) {
                return false;
            }
            return food.getFoodCategory().getName().equals(category);
        }).toList();
    }
    
    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }
    
    @Override
    public Food findFoodById(Long id) throws Exception {
        return foodRepository.findById(id).orElseThrow(() -> new Exception("Food not found"));
    }
    
    @Override
    public Food updateAvailability(Long id) throws Exception {
        Food food = findFoodById(id);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
