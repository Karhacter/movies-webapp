package com.karhacter.movies_webapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies/images")
public class MovieImageController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/{movieId}/gallery")
    public ResponseEntity<MovieDTO> addGalleryImages(
            @PathVariable Long movieId,
            @Valid @RequestBody List<String> imageUrls) {
        MovieDTO updatedMovie = movieService.addGalleryImages(movieId, imageUrls);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}/gallery/{imageUrl}")
    public ResponseEntity<MovieDTO> removeGalleryImage(
            @PathVariable Long movieId,
            @PathVariable String imageUrl) {
        MovieDTO updatedMovie = movieService.removeGalleryImage(movieId, imageUrl);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @PutMapping("/{movieId}/main")
    public ResponseEntity<MovieDTO> updateMainImage(
            @PathVariable Long movieId,
            @RequestParam String newImageUrl) {
        MovieDTO updatedMovie = movieService.updateMainImage(movieId, newImageUrl);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }
}