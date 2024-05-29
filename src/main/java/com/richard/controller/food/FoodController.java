package com.richard.controller.food;

import com.richard.model.Food;
import com.richard.model.User;
import com.richard.service.FoodService;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {
    
    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    
    public FoodController(FoodService foodService, UserService userService, RestaurantService restaurantService) {
        this.foodService = foodService;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        List<Food> foods = foodService.searchFood(name);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
    
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Food>> getRestaurantFood(
            @RequestParam boolean vegetarian,
            @RequestParam boolean seasonal,
            @RequestParam boolean nonVegetarian,
            @RequestParam(required = false) String category,
            @PathVariable("id") Long restaurantId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        List<Food> foods = foodService.getRestaurantFood(restaurantId, vegetarian, seasonal, nonVegetarian, category);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
    
}
