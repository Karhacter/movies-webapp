package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.payloads.WatchlistDTO;

public interface WatchListService {

    String DeleteList(Long id);

    // logic add movie to watch list or favorite

    List<WatchlistDTO> getUserWatchlist(Long userId);

    WatchlistDTO addMovieToWatchlist(Long userId, Long movieId);
}
