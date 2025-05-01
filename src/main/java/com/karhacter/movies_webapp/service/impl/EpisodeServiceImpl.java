package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.dto.EpisodeDTO;
import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.repository.EpsiodeRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.EpisodeService;

import jakarta.transaction.Transactional;

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
        Optional<Episode> existedEpisode = Optional
                .ofNullable(episodeRepo.findByEpisodeNumber(episode.getEpisodeNumber()));
        if (existedEpisode.isPresent()) {
            throw new APIException("Episode with the number '" + episode.getEpisodeNumber() + "' already exists !!!");
        }
        Episode savedEpisode = episodeRepo.save(episode);
        return modelMapper.map(savedEpisode, EpisodeDTO.class);
    }

    @Override
    public List<EpisodeDTO> getAllEp() {
        return episodeRepo.findAll().stream().map(episode -> modelMapper.map(episode, EpisodeDTO.class)).toList();
    }

    @Override
    public EpisodeDTO updateEp(Long id, Episode episode) {
        Optional<Episode> existedEpisode = episodeRepo.findById(id);
        if (existedEpisode.isEmpty()) {
            throw new APIException("Episode with id '" + id + "' not found !!!");
        }
        episode.setId(id);
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

}
