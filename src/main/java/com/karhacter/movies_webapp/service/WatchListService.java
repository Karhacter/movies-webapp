package com.karhacter.movies_webapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.karhacter.movies_webapp.dto.WatchlistDTO;

public interface WatchListService {

    String DeleteList(Long id);

    // logic add movie to watch list or favorite

    Page<WatchlistDTO> getUserWatchlist(Long userId, Pageable pageable);

    WatchlistDTO addMovieToWatchlist(Long userId, Long movieId);

    String removeMovieFromWatchlist(Long userId, Long movieId);

    // Optional: for frontend toggle functionality
    WatchlistDTO toggleWatchlist(Long userId, Long movieId);

    boolean isMovieInWatchlist(Long userId, Long movieId);
}
