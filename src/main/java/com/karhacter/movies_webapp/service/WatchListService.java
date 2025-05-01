package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.dto.WatchlistDTO;

public interface WatchListService {

    String DeleteList(Long id);

    // logic add movie to watch list or favorite

    List<WatchlistDTO> getUserWatchlist(Long userId);

    WatchlistDTO addMovieToWatchlist(Long userId, Long movieId);

    String removeMovieFromWatchlist(Long userId, Long movieId);

    // Optional: for frontend toggle functionality
    WatchlistDTO toggleWatchlist(Long userId, Long movieId);

    boolean isMovieInWatchlist(Long userId, Long movieId);
}
