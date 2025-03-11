package com.karhacter.movies_webapp.service.impl;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.MovieDTO;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.MovieService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieSerivceImpl implements MovieService {

    @Autowired
    private MovieRepo movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MovieSerivceImpl(MovieRepo movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public MovieDTO createMovie(Long categoryId, Movie movie) {
        // Category category = CategoryRepo.findById(categoryId)
        // .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId",
        // categoryId));

        // check if the movie have exist
        Optional<Movie> existedMovie = Optional
                .ofNullable(movieRepository.findByTitle(movie.getTitle()));

        if (existedMovie.isPresent()) {
            throw new APIException("Movie with the name '" +
                    movie.getTitle() + "' already exists !!!");
        }

        Movie savedMovie = movieRepository.save(movie);

        return modelMapper.map(savedMovie, MovieDTO.class);
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll(); // Fetch all movies from DB
        return movies.stream()
                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

        return modelMapper.map(movie, MovieDTO.class);
    }

    @Override
    public MovieDTO updateMovie(Long id, Movie movie) {
        Movie existedMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

        existedMovie.setTitle(movie.getTitle());
        existedMovie.setDescription(movie.getDescription());
        existedMovie.setReleaseDate(movie.getReleaseDate());
        existedMovie.setRating(movie.getRating());
        existedMovie.setCategory(movie.getCategory());

        Movie updatedMovie = movieRepository.save(existedMovie);

        return modelMapper.map(updatedMovie, MovieDTO.class);
    }

    @Override
    public String deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

        movieRepository.delete(movie);

        return "Movie with id: " + id + " has been deleted successfully !!!";
    }
}
