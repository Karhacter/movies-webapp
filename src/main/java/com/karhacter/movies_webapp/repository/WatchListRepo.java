package com.karhacter.movies_webapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.entity.Watchlist;

@Repository
public interface WatchListRepo extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser(User user);

    Page<Watchlist> findByUser(User user, Pageable pageable);

    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<Watchlist> findByUserAndMovie(User user, Movie movie);
}
