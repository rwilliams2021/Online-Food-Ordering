package com.richard.service;

import com.richard.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name, Long userId) throws Exception;
    List<Category> findCategoriesByRestaurantId(Long id) throws Exception;
    Category findCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id);
}
