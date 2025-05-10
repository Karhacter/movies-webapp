package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.service.MovieImageService;

@RestController
@RequestMapping("/api/movies/images")
public class MovieImageController {

    @Autowired
    private MovieImageService movieImage;

    @PostMapping(value = "/{movieId}/gallery", consumes = { "multipart/form-data" })
    public ResponseEntity<MovieDTO> addGalleryImages(
            @PathVariable Long movieId,
            @RequestParam("files") List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validate file types
        for (MultipartFile file : files) {
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }
        }

        MovieDTO updatedMovie = movieImage.addGalleryImages(movieId, files);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{movieId}/gallery")
    public ResponseEntity<MovieDTO> removeGalleryImage(
            @PathVariable Long movieId,
            @RequestParam String imageUrl) {
        MovieDTO result = movieImage.removeGalleryImage(movieId, imageUrl);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{movieId}/main-image")
    public ResponseEntity<MovieDTO> updateMainImage(
            @PathVariable Long movieId,
            @RequestParam("imageUrl") String imageUrl) {
        MovieDTO result = movieImage.updateMainImage(movieId, imageUrl);
        return ResponseEntity.ok(result);
    }
}