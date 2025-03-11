package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.payloads.MovieDTO;

public interface MovieService {
    MovieDTO createMovie(Long categoryId, Movie movie);

    // get all movies
    List<MovieDTO> getAllMovies();

    // get one movie
    MovieDTO getMovieById(Long id);

    // update movie
    MovieDTO updateMovie(Long id, Movie movie);

    // delete movie
    String deleteMovie(Long id);
}
