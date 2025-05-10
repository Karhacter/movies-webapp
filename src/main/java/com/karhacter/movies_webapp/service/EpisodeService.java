package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.dto.EpisodeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EpisodeService {
    EpisodeDTO createEp(Episode episode);

    Page<EpisodeDTO> getEpisodeDTO(Pageable pageable);

    EpisodeDTO updateEp(Long id, Episode episode);

    String deleteEp(Long id);

    List<EpisodeDTO> getEpisodesByMovieId(Long movieId);

    List<EpisodeDTO> getEpisodesByMovieSlug(String slug);

    EpisodeDTO getEpisodeById(Long id);

    EpisodeDTO uploadVideo(Long epId, MultipartFile videoFile);
}
