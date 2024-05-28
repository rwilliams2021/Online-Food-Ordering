package com.richard.controller.restaurant;

import com.richard.model.Restaurant;
import com.richard.model.User;
import com.richard.request.CreateRestaurantRequest;
import com.richard.response.MessageResponse;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/restaurant")
public class AdminRestaurantController {
    
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestBody CreateRestaurantRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        Restaurant restaurant = restaurantService.createRestaurant(req, user);
        return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @RequestBody CreateRestaurantRequest req,
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        Restaurant restaurant = restaurantService.updateRestaurant(id, req);
        return ResponseEntity.ok(restaurant);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        restaurantService.deleteRestaurant(id);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Restaurant deleted successfully");
        return ResponseEntity.ok(messageResponse);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return ResponseEntity.ok(restaurant);
    }
    
    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserId(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());
        return ResponseEntity.ok(restaurant);
    }
    
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }
}
