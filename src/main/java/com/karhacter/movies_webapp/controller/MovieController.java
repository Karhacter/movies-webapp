package com.karhacter.movies_webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.payloads.MovieDTO;
import com.karhacter.movies_webapp.service.MovieService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("movies/add")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody Movie movie) {
        MovieDTO savedMovieDTO = movieService.createMovie((long) 12, movie);
        return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
    }

    // get all movies
    @GetMapping("movies/index")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        logger.info("Received request for movies - Page: {}, Size: {}, SortBy: {}, SortOrder: {}",
                page, size, sortBy, sortOrder);

        // Ensure page is not negative
        page = Math.max(0, page);

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MovieDTO> movies = movieService.getAllMovies(pageable);

        logger.info("Returning movies - Current Page: {}, Total Pages: {}, Total Elements: {}",
                movies.getNumber(), movies.getTotalPages(), movies.getTotalElements());

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // get one movie
    @GetMapping("movie/detail/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        MovieDTO detailMovieDTO = movieService.getMovieById(id);
        return new ResponseEntity<>(detailMovieDTO, HttpStatus.OK);
    }

    // update movie
    @PostMapping("movie/update/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        MovieDTO updatedMovieDTO = movieService.updateMovie(id, movie);
        return new ResponseEntity<>(updatedMovieDTO, HttpStatus.OK);
    }

    // delete movie
    @PostMapping("movie/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        String message = movieService.deleteMovie(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // get movie by slug
    @GetMapping("movie/slug/{slug}")
    public ResponseEntity<MovieDTO> getMovieBySlug(@PathVariable String slug) {
        MovieDTO movieDTO = movieService.getMovieBySlug(slug);
        return new ResponseEntity<>(movieDTO, HttpStatus.OK);
    }
}