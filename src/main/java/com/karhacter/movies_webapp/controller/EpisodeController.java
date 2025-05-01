package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.EpisodeDTO;
import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.service.EpisodeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/api/episodes")
@RestController
public class EpisodeController {
    @Autowired
    private EpisodeService episodeService;

    @PostMapping("/add")
    public ResponseEntity<EpisodeDTO> CreateEp(@Valid @RequestBody Episode episode) {
        EpisodeDTO savedEpDTO = episodeService.createEp(episode);
        return new ResponseEntity<>(savedEpDTO, HttpStatus.CREATED);
    }

    // Ep = episode

    @GetMapping("/index")
    public ResponseEntity<List<EpisodeDTO>> getAllCategories() {
        List<EpisodeDTO> savedEpDTO = episodeService.getAllEp();
        return new ResponseEntity<>(savedEpDTO, HttpStatus.OK);
    }

    // get episode of that movie from id
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesByMovieId(@PathVariable Long movieId) {
        List<EpisodeDTO> episodes = episodeService.getEpisodesByMovieId(movieId);
        return new ResponseEntity<>(episodes, HttpStatus.OK);
    }

    @GetMapping("/movie/slug/{slug}")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesByMovieId(@PathVariable String slug) {
        List<EpisodeDTO> episodes = episodeService.getEpisodesByMovieSlug(slug);
        return new ResponseEntity<>(episodes, HttpStatus.OK);
    }

    // update category
    @PutMapping("/update/{epId}")
    public ResponseEntity<EpisodeDTO> updateEpisode(@PathVariable Long epId,
            @Valid @RequestBody Episode epsiode) {
        EpisodeDTO updatedEpDTO = episodeService.updateEp(epId, epsiode);
        return new ResponseEntity<>(updatedEpDTO, HttpStatus.OK);
    }

    // delete category
    @DeleteMapping("/delete/{epId}")
    public ResponseEntity<String> deleteEp(@PathVariable Long epId) {
        episodeService.deleteEp(epId);
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

}
