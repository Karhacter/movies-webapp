package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.payloads.EpisodeDTO;

public interface EpisodeService {
    EpisodeDTO createEp(Episode episode);

    List<EpisodeDTO> getAllEp();

    EpisodeDTO getById(Long id);

    EpisodeDTO updateEp(Long id, Episode episode);

    String deleteEp(Long id);
}
