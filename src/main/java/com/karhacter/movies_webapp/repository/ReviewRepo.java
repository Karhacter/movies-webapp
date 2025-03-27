package com.karhacter.movies_webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Review;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByMovieId(Long movieId);

    Optional<Review> findByUserAndMovie(User user, Movie movie);

    boolean existsByUserAndMovie(User user, Movie movie);
}
