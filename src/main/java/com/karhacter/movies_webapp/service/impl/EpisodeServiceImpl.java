package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.payloads.EpisodeDTO;
import com.karhacter.movies_webapp.repository.EpsiodeRepo;
import com.karhacter.movies_webapp.service.EpisodeService;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    @Autowired
    private EpsiodeRepo episodeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EpisodeDTO createEp(Episode episode) {
        Optional<Episode> existedEpisode = Optional.ofNullable(episodeRepo.findByEpNumber(episode.getEpisodeNumber()));
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
    public EpisodeDTO getById(Long id) {
        Optional<Episode> existedEpisode = episodeRepo.findById(id);
        if (existedEpisode.isEmpty()) {
            throw new APIException("Episode with id '" + id + "' not found !!!");
        }
        return modelMapper.map(existedEpisode.get(), EpisodeDTO.class);
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
