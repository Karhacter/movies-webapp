package com.karhacter.movies_webapp.service;

import com.karhacter.movies_webapp.entity.History;

import java.util.List;

public interface HistoryService {
    List<History> getUserHistory(Long userId);

    History saveWatchedMovie(Long userId, Long movieId, int watchProgress);
}