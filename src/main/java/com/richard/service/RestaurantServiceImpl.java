package com.richard.service;

import com.richard.dto.RestaurantDto;
import com.richard.model.Address;
import com.richard.model.Restaurant;
import com.richard.model.User;
import com.richard.request.CreateRestaurantRequest;
import com.richard.respository.AddressRepository;
import com.richard.respository.RestaurantRepository;
import com.richard.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest req, User user) {
        Address address = addressRepository.save(req.getAddress());
        
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(req.getContactInformation());
        restaurant.setCuisineType(req.getCuisineType());
        restaurant.setDescription(req.getDescription());
        restaurant.setImages(req.getImages());
        restaurant.setName(req.getName());
        restaurant.setOpeningHours(req.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);
        
        return restaurantRepository.save(restaurant);
    }
    
    @Override
    public Restaurant updateRestaurant(Long id, CreateRestaurantRequest updatedRestaurant) throws Exception {
        
        Restaurant restaurant = findRestaurantById(id);
        if (restaurant.getCuisineType() != null) {
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        }
        if (restaurant.getDescription() != null) {
            restaurant.setDescription(updatedRestaurant.getDescription());
        }
        if (restaurant.getName() != null) {
            restaurant.setName(updatedRestaurant.getName());
        }
        return restaurantRepository.save(restaurant);
    }
    
    @Override
    public void deleteRestaurant(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }
    
    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    
    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.orElseThrow(() -> new Exception("Restaurant not found"));
    }
    
    @Override
    public Restaurant getRestaurantByUserId(Long id) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(id);
        if (restaurant == null) {
            throw new Exception("Restaurant not found");
        }
        return restaurant;
    }
    
    /*
     * Add or remove restaurant from favourites
     * If restaurant is already in favourites, remove it
     * @param id
     * @param user
     * @return RestaurantDto
     */
    @Override
    public RestaurantDto addToFavourites(Long id, User user) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        
        RestaurantDto dto = new RestaurantDto();
        dto.setDescription(restaurant.getDescription());
        dto.setTitle(restaurant.getName());
        dto.setImages(restaurant.getImages());
        dto.setId(restaurant.getId());
        
        if (user.getFavourites().contains(dto)) {
            user.getFavourites().remove(dto);
        } else {
            user.getFavourites().add(dto);
        }
        userRepository.save(user);
        return dto;
    }
    
    @Override
    public void deleteAllFavourites(User user) {
        user.getFavourites().clear();
        userRepository.save(user);
    }
    
    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
