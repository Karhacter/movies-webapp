package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.payloads.CategoryDTO;

public interface CategoryService {
    // add new category
    CategoryDTO createCategory(Category category);

    // get all categories
    List<CategoryDTO> getAllCategories();

    // get category by id
    CategoryDTO getCategoryById(Long id);

    // update category
    CategoryDTO updateCategory(Long id, Category category);

    // delete category
    String deleteCategory(Long id);
}
