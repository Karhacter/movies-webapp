package com.karhacter.movies_webapp.controller;

import com.karhacter.movies_webapp.config.AppConstant;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.payloads.MovieDTO;
import com.karhacter.movies_webapp.payloads.MovieResponse;
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
    public ResponseEntity<MovieResponse> getAllMovies(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_MOVIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {

        pageNumber = pageNumber > 0 ? pageNumber - 1 : 0;

        MovieResponse movieResponse = movieService.getAllMovies(
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);
        return new ResponseEntity<MovieResponse>(movieResponse, HttpStatus.OK);
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