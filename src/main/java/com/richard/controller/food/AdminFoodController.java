package com.richard.controller.food;

import com.richard.model.Food;
import com.richard.model.Restaurant;
import com.richard.model.User;
import com.richard.request.CreateFoodRequest;
import com.richard.response.MessageResponse;
import com.richard.service.FoodService;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/food")
public class AdminFoodController {
    
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    
    @PostMapping("/create")
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Food food = foodService.createFood(req, req.getCategory(), restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable("id") Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id);
        
        MessageResponse rsp = new MessageResponse();
        rsp.setMessage("Food deleted successfully");
        
        return new ResponseEntity<>(rsp, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable("id") Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Food food = foodService.updateAvailability(id);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
    
}
