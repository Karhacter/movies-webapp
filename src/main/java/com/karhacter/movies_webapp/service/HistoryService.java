package com.karhacter.movies_webapp.service;

import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.payloads.HistoryDTO;

import java.util.List;

public interface HistoryService {
    List<HistoryDTO> getUserHistory(Long userId);

    History saveWatchedMovie(Long userId, Long movieId, int watchProgress);
}