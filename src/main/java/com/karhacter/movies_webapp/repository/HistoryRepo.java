package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;

@Repository
public interface HistoryRepo extends JpaRepository<History, Long> {
    List<History> findByUser(User user);

    Page<History> findByUser(User user, Pageable pageable);

    boolean existsByUserAndMovie(User user, Movie movie);
}
