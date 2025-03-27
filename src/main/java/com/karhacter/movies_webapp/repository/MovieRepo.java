package com.karhacter.movies_webapp.repository;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    Movie findByTitle(String title);

    Optional<Movie> findBySlug(String slug);

    // optional 1: use List
    // List<Movie> findByCategory(Category category);

    // optional 2: use Query
    @Query("SELECT m FROM Movie m WHERE m.category = :category")
    List<Movie> findByCategory(@Param("category") Category category);
}
