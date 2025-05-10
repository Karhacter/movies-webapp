package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/index")
    public ResponseEntity<Page<EpisodeDTO>> getEpisodes(Pageable pageable) {
        Page<EpisodeDTO> episodesPage = episodeService.getEpisodeDTO(pageable);
        return new ResponseEntity<>(episodesPage, HttpStatus.OK);
    }

    @PostMapping("/{epId}/upload-video")
    public ResponseEntity<EpisodeDTO> uploadVideo(@PathVariable Long epId,
            @RequestParam("videoFile") MultipartFile videoFile) {
        EpisodeDTO updatedEpisode = episodeService.uploadVideo(epId, videoFile);
        return new ResponseEntity<>(updatedEpisode, HttpStatus.OK);
    }

    // get episode of that movie from id
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesByMovieId(@PathVariable Long movieId) {
        List<EpisodeDTO> episodes = episodeService.getEpisodesByMovieId(movieId);
        return new ResponseEntity<>(episodes, HttpStatus.OK);
    }

    @GetMapping("/movie/slug/{slug}")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesByMovieSlug(@PathVariable String slug) {
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
        return new ResponseEntity<>("Episode deleted successfully", HttpStatus.OK);
    }

    // get episode by id (for video play)
    @GetMapping("/{epId}")
    public ResponseEntity<EpisodeDTO> getEpisodeById(@PathVariable Long epId) {
        EpisodeDTO episodeDTO = episodeService.getEpisodeById(epId);
        return new ResponseEntity<>(episodeDTO, HttpStatus.OK);
    }
}
