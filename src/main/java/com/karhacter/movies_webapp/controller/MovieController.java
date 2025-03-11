package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody Movie movie) {
        MovieDTO savedMovieDTO = movieService.createMovie((long) 12, movie);
        return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
    }

    @GetMapping("/index")
    public List<MovieDTO> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        MovieDTO detailMovieDTO = movieService.getMovieById(id);
        return new ResponseEntity<>(detailMovieDTO, HttpStatus.OK);
    }

}