package com.richard.controller.restaurant;

import com.richard.dto.RestaurantDto;
import com.richard.model.Restaurant;
import com.richard.model.User;
import com.richard.response.MessageResponse;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private UserService userService;
    
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
    public ResponseEntity<RestaurantDto> addToFavourites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        RestaurantDto dto = restaurantService.addToFavourites(id, user);
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/clear-favourites")
    public ResponseEntity<MessageResponse> deleteAllFavourites(
            @RequestHeader("Authorization") String jwt) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        restaurantService.deleteAllFavourites(user);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Favourites cleared successfully");
        return ResponseEntity.ok(messageResponse);
    }
}
