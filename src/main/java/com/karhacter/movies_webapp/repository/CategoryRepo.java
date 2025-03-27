package com.karhacter.movies_webapp.repository;

import org.springframework.stereotype.Repository;
import com.karhacter.movies_webapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
