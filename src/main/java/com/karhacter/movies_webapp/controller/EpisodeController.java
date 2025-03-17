package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.karhacter.movies_webapp.entity.Episode;

import com.karhacter.movies_webapp.payloads.EpisodeDTO;
import com.karhacter.movies_webapp.service.EpisodeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("/api/episodes")
@RestController
@CrossOrigin(origins = "*")
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

    // get one cate
    @GetMapping("/detail/{id}")
    public ResponseEntity<EpisodeDTO> getByEpId(@PathVariable Long id) {
        EpisodeDTO detailEpDTO = episodeService.getById(id);
        return new ResponseEntity<>(detailEpDTO, HttpStatus.OK);
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
