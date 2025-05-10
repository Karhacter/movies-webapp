package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.dto.CategoryDTO;
import com.karhacter.movies_webapp.repository.CategoryRepo;
import com.karhacter.movies_webapp.service.CategoryService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(Category category) {
        // check if the category exists
        Optional<Category> existingCategory = Optional
                .ofNullable(categoryRepo.findByName(category.getName()));

        if (existingCategory.isPresent()) {
            throw new APIException("Category with the name '" +
                    category.getName() + "' already exists !!!");
        }

        // Lưu danh mục mới
        Category savedCategory = categoryRepo.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new APIException("Category with id '" + id + "' not found !!!"));

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long id, Category category) {
        // check if the category exists
        Category savedCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));

        // update category
        category.setId(id);
        savedCategory = categoryRepo.save(category);

        // save the updated category

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long id) {
        // check if the category exists
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new APIException("Category with id '" + id + "' not found !!!"));

        // delete category
        categoryRepo.delete(existingCategory);

        return "Category with id '" + id + "' deleted successfully !!!";
    }
}
