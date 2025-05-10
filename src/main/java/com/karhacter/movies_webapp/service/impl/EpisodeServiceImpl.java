package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.EpisodeDTO;
import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.repository.EpsiodeRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.EpisodeService;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class EpisodeServiceImpl implements EpisodeService {
    @Autowired
    private EpsiodeRepo episodeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepo movieRepo;

    @Override
    public EpisodeDTO createEp(Episode episode) {
        List<Episode> existedEpisodes = episodeRepo.findByEpisodeNumberAndMovie(episode.getEpisodeNumber(), episode.getMovie());
        if (!existedEpisodes.isEmpty()) {
            throw new APIException("Episode with the number '" + episode.getEpisodeNumber() + "' already exists for this movie!!!");
        }
        Episode savedEpisode = episodeRepo.save(episode);
        return modelMapper.map(savedEpisode, EpisodeDTO.class);
    }

    @Override
    public Page<EpisodeDTO> getEpisodeDTO(Pageable pageable) {
        Page<Episode> episodesPage = episodeRepo.findDistinctByMovie(pageable);

        Page<EpisodeDTO> episodeDTOPage = episodesPage.map(episode -> {
            EpisodeDTO dto = modelMapper.map(episode, EpisodeDTO.class);
            Long movieId = episode.getMovie().getId();
            long totalSeasons = episodeRepo.countDistinctSeasonsByMovieId(movieId);
            long totalEpisodes = episodeRepo.countByMovieId(movieId);
            dto.setSeason((int) totalSeasons);
            dto.setEpisodeNumber((int) totalEpisodes);
            return dto;
        });

        return episodeDTOPage;
    }

    @Override
    public EpisodeDTO updateEp(Long id, Episode episode) {
        Optional<Episode> existedEpisodeOpt = episodeRepo.findById(id);
        if (existedEpisodeOpt.isEmpty()) {
            throw new APIException("Episode with id '" + id + "' not found !!!");
        }
        Episode existedEpisode = existedEpisodeOpt.get();
        // Update only allowed fields
        existedEpisode.setSeason(episode.getSeason());
        existedEpisode.setEpisodeNumber(episode.getEpisodeNumber());
        existedEpisode.setVideoUrl(episode.getVideoUrl());
        Episode savedEpisode = episodeRepo.save(existedEpisode);
        return modelMapper.map(savedEpisode, EpisodeDTO.class);
    }

    @Override
    public EpisodeDTO uploadVideo(Long epId, MultipartFile videoFile) {
        Optional<Episode> episodeOpt = episodeRepo.findById(epId);
        if (episodeOpt.isEmpty()) {
            throw new APIException("Episode with id '" + epId + "' not found !!!");
        }
        Episode episode = episodeOpt.get();

        // Save video file to absolute "video" directory
        String uploadDir = System.getProperty("user.dir") + "/video";
        String originalFilename = videoFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new APIException("Invalid video file name");
        }

        // Sanitize and shorten filename
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String fileName = System.currentTimeMillis() + extension;

        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            videoFile.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new APIException("Could not save video file: " + e.getMessage());
        }

        // Update videoUrl field
        episode.setVideoUrl("/video/" + fileName);
        Episode savedEpisode = episodeRepo.save(episode);
        return modelMapper.map(savedEpisode, EpisodeDTO.class);
    }

    @Override
    public List<EpisodeDTO> getEpisodesByMovieId(Long movieId) {
        // Fetch the movie by its ID
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Fetch episodes by movie
        List<Episode> episodes = episodeRepo.findByMovie(movie);

        // Convert entities to DTOs using ModelMapper
        return episodes.stream()
                .map(episode -> modelMapper.map(episode, EpisodeDTO.class))
                .collect(Collectors.toList());
    }

    //
    @Override
    public List<EpisodeDTO> getEpisodesByMovieSlug(String slug) {
        // Fetch the movie by its ID
        Movie movie = movieRepo.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Fetch episodes by movie
        List<Episode> episodes = episodeRepo.findByMovie(movie);

        // Convert entities to DTOs using ModelMapper
        return episodes.stream()
                .map(episode -> modelMapper.map(episode, EpisodeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteEp(Long id) {
        Optional<Episode> existedEpisode = episodeRepo.findById(id);
        if (existedEpisode.isEmpty()) {
            throw new APIException("Episode with id '" + id + "' not found !!!");
        }
        episodeRepo.delete(existedEpisode.get());
        return "Episode with id '" + id + "' deleted successfully !!!";
    }

    @Override
    public EpisodeDTO getEpisodeById(Long id) {
        Optional<Episode> episodeOpt = episodeRepo.findById(id);
        if (episodeOpt.isEmpty()) {
            throw new APIException("Episode with id '" + id + "' not found !!!");
        }
        return modelMapper.map(episodeOpt.get(), EpisodeDTO.class);
    }
}
