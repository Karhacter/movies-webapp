package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.payloads.CategoryDTO;
import com.karhacter.movies_webapp.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody Category category) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(category);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @GetMapping("/index")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> savedCategoryDTO = categoryService.getAllCategories();
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    // get one cate
    @GetMapping("/detail/{id}")
    public ResponseEntity<CategoryDTO> getCateyById(@PathVariable Long id) {
        CategoryDTO detailCategoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(detailCategoryDTO, HttpStatus.OK);
    }

    // update category
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
            @Valid @RequestBody Category category) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId, category);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }

    // delete category
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }
}
