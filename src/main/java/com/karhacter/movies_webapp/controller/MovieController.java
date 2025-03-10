package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    
    @GetMapping("/index")
    public List<Movie> getAllMovies() {
        return movieService.gellAllMovies();
    }
    
}
