package com.karhacter.movies_webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Review;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByMovieId(Long movieId);

    Optional<Review> findByUserAndMovie(User user, Movie movie);

    boolean existsByUserAndMovie(User user, Movie movie);

    // Search reviews by content, username, movie title, review date, rating
    @Query("SELECT r FROM Review r WHERE " +
            "(:content IS NULL OR r.comment LIKE CONCAT('%', :content, '%')) AND " +
            "(:rating IS NULL OR r.rating = :rating)")
    List<Review> searchReviews(@Param("content") String content,
            @Param("rating") Integer rating);
}
