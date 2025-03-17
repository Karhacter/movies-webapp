package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.entity.Watchlist;
import com.karhacter.movies_webapp.payloads.WatchlistDTO;

@Repository
public interface WatchListRepo extends JpaRepository<Watchlist, Long> {
    List<WatchlistDTO> findByUser(User user);

    boolean existsByUserAndMovie(User user, Movie movie);
}
