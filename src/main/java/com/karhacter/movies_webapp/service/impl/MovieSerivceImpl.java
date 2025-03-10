package com.karhacter.movies_webapp.service.impl;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.repository.MovieRepository;
import com.karhacter.movies_webapp.service.MovieService;

import java.util.List;  
import org.springframework.stereotype.Service;


@Service

public class MovieSerivceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieSerivceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> gellAllMovies() {
        return movieRepository.findAll();
    }

}
