package com.richard.controller.restaurant;

import com.richard.dto.RestaurantDto;
import com.richard.model.Restaurant;
import com.richard.model.User;
import com.richard.response.MessageResponse;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/restaurant")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    private final UserService userService;
    
    public RestaurantController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurant(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword) throws Exception {
        
        List<Restaurant> restaurants = restaurantService.searchRestaurant(keyword);
        return ResponseEntity.ok(restaurants);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }
    
    @PutMapping("/{id}/add-favourites")
    public ResponseEntity<List<RestaurantDto>> addToFavourites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        List<RestaurantDto> favRestaurants = restaurantService.addToFavourites(id, user);
        return ResponseEntity.ok(favRestaurants);
    }
    
    @DeleteMapping("/clear-favourites")
    public ResponseEntity<MessageResponse> deleteAllFavourites(
            @RequestHeader("Authorization") String jwt) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        restaurantService.deleteAllFavourites(user);
        MessageResponse messageResponse = new MessageResponse("Favourites cleared successfully");
        return ResponseEntity.ok(messageResponse);
    }
    
    @GetMapping("/favourites")
    public ResponseEntity<List<RestaurantDto>> getAllFavourites(
            @RequestHeader("Authorization") String jwt) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        List<RestaurantDto> favourites = restaurantService.getAllFavourites(user);
        return ResponseEntity.ok(favourites);
    }
}
