package com.richard.controller;

import com.richard.dto.RestaurantDto;
import com.richard.model.Restaurant;
import com.richard.model.User;
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
        
        User user = userService.findUserByJwtToken(jwt);
        List<Restaurant> restaurants = restaurantService.searchRestaurant(keyword);
        return ResponseEntity.ok(restaurants);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findRestaurantById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }
    
    @PutMapping("/{id}/add-favourites")
    public ResponseEntity<RestaurantDto> addToFavourites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable("id") Long id) throws Exception {
        
        User user = userService.findUserByJwtToken(jwt);
        RestaurantDto dto = restaurantService.addToFavourites(id, user);
        return ResponseEntity.ok(dto);
    }
}
