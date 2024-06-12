package com.richard.service;

import com.richard.enums.FoodCategory;
import com.richard.model.Category;
import com.richard.model.Food;
import com.richard.model.Restaurant;
import com.richard.request.CreateFoodRequest;
import com.richard.respository.FoodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FoodServiceImpl implements FoodService {
    
    private final FoodRepository foodRepository;
    
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }
    
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
        if (req.isVegetarian()) {
            food.setVegetarian(true);
        } else {
            food.setNonVegetarian(true);
        }
        food.setCreationDate(LocalDateTime.now());
        
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
        
        Stream<Food> foodStream = foods.stream();
        
        if (isVegetarian) {
            foodStream = foodStream.filter(Food::isVegetarian);
        }
        if (isNonVegetarian) {
            foodStream = foodStream.filter(Food::isNonVegetarian);
        }
        if (isSeasonal) {
            foodStream = foodStream.filter(Food::isSeasonal);
        }
        if (category != null && !category.isEmpty() && !category.toUpperCase().equals(FoodCategory.ALL.name())) {
            if (isValidFoodCategory(category)) {
                foodStream = filterByCategory(foodStream, category);
            }
        }
        
        return foodStream.collect(Collectors.toList());
    }
    
    private boolean isValidFoodCategory(String category) {
        try {
            FoodCategory.valueOf(category.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid food category", e);
        }
    }
    
    private Stream<Food> filterByCategory(Stream<Food> foods, String category) {
        return foods.filter(food -> food.getFoodCategory().getName().equals(category));
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
    public Food updateAvailability(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
