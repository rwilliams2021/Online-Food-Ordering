package com.richard.controller;

import com.richard.model.Category;
import com.richard.model.User;
import com.richard.service.CategoryService;
import com.richard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    
    private final CategoryService categoryService;
    private final UserService userService;
    
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }
    
    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        Category createdCategory = categoryService.createCategory(category.getName(), user.getId());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
    
    @GetMapping("/category/restaurant")
    public ResponseEntity<List<Category>> getRestaurantCategories(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found"));
        List<Category> categories = categoryService.findCategoriesByRestaurantId(user.getId());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
