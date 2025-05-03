package com.karhacter.movies_webapp.repository;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    Movie findByTitle(String title);

    Optional<Movie> findBySlug(String slug);

    List<Movie> findByParentID(Movie parentID);

    // Optional 1: Uses Comparator.comparing for more readable sorting logic
    List<Movie> findByParentID_Id(Long parentId);

    // Optional 2: Alternative Version with Custom Query
    // Database-Level Sorting: More efficient than in-memory sorting
    // Cleaner Code: Less business logic in service layer
    // Null Safety: Handles null seasonNumbers in query
    @Query("SELECT m FROM Movie m WHERE m.parentID.id = :parentId ORDER BY COALESCE(m.seasonNumber, 0) ASC")
    List<Movie> findSeasonsByParentId(@Param("parentId") Long parentId);

    @Query("SELECT m FROM Movie m JOIN m.categories c WHERE c = :category")
    List<Movie> findByCategory(@Param("category") Category category);

    @Query("SELECT m FROM Movie m WHERE m.statusDelete = 1")
    List<Movie> findAllActive();

    // List<Movie> findByStatusDelete(int statusDelete);
    Page<Movie> findByStatusDelete(int status, Pageable pageable);

}
