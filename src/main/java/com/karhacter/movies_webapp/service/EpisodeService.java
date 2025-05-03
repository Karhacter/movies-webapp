package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.dto.EpisodeDTO;

public interface EpisodeService {
    EpisodeDTO createEp(Episode episode);

    List<EpisodeDTO> getAllEp();

    EpisodeDTO updateEp(Long id, Episode episode);

    String deleteEp(Long id);

    List<EpisodeDTO> getEpisodesByMovieId(Long movieId);

    List<EpisodeDTO> getEpisodesByMovieSlug(String slug);
}
