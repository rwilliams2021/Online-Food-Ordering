package com.richard.controller;

import com.richard.model.IngredientCategory;
import com.richard.model.IngredientItem;
import com.richard.request.IngredientCategoryRequest;
import com.richard.request.IngredientRequest;
import com.richard.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientItem> createIngredientItem(
            @RequestBody IngredientRequest req) throws Exception {
        IngredientItem item = ingredientService.createIngredientItem(req.getName(), req.getCategoryId(), req.getRestaurantId());
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/stock")
    public ResponseEntity<IngredientItem> updateIngredientStock(
            @PathVariable("id") Long id) throws Exception {
        IngredientItem item = ingredientService.updateStock(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
    
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientItem>> getRestaurantIngredients(
            @PathVariable("id") Long id) throws Exception {
        List<IngredientItem> items = ingredientService.findRestaurantIngredients(id);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody IngredientCategoryRequest req) throws Exception {
        IngredientCategory category = ingredientService.createIngredientCategory(req.getName(), req.getRestaurantId());
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory(
            @PathVariable("id") Long id) throws Exception {
        List<IngredientCategory> categories = ingredientService.findIngredientCategoriesByRestaurantId(id);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
