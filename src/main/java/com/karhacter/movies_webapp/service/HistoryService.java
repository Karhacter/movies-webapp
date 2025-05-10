package com.karhacter.movies_webapp.service;

import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.dto.HistoryDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {
    List<History> getUserHistory(Long userId);

    History saveWatchedMovie(Long userId, Long movieId, int watchProgress);

    Page<HistoryDTO> getUserHistoryDTO(Long userId, Pageable pageable);

    // New delete method
    String deleteHistoryById(Long id);
}
