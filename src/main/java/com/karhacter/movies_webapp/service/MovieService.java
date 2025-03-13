package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.payloads.MovieDTO;

public interface MovieService {

    // add new movie
    MovieDTO createMovie(Long categoryId, Movie movie);

    // get all movies
    List<MovieDTO> getAllMovies();

    // get one movie
    MovieDTO getMovieById(Long id);

    // update movie
    MovieDTO updateMovie(Long id, Movie movie);

    // delete movie
    String deleteMovie(Long id);

    // get all movies by category
    // update token when user purchase token to watch ep movie
    // get new movies
    // get the top 10 movies
    // random movie
    // get all movies by year
    // get all movies if movie title contains the search keyword
    // get all movies between the tv/series and Movie/Ova
}
