package com.karhacter.movies_webapp.repository;

import com.karhacter.movies_webapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    Movie findByTitle(String title);
}
