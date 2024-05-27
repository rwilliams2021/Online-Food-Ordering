package com.richard.service;

import com.richard.dto.RestaurantDto;
import com.richard.model.Restaurant;
import com.richard.model.User;
import com.richard.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {
    Restaurant createRestaurant(CreateRestaurantRequest req, User user);
    Restaurant updateRestaurant(Long id, CreateRestaurantRequest req) throws Exception;
    void deleteRestaurant(Long id) throws Exception;
    List<Restaurant> getAllRestaurants();
    List<Restaurant> searchRestaurant(String keyword);
    Restaurant findRestaurantById(Long id) throws Exception;
    Restaurant getRestaurantByUserId(Long id) throws Exception;
    RestaurantDto addToFavourites(Long id, User user) throws Exception;
    void deleteAllFavourites(User user) throws Exception;
    Restaurant updateRestaurantStatus(Long id) throws Exception;
}
