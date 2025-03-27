package com.karhacter.movies_webapp.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.payloads.MovieDTO;
import com.karhacter.movies_webapp.payloads.MovieResponse;
import com.karhacter.movies_webapp.payloads.MovieStatsDTO;

public interface MovieService {

    // add new movie
    MovieDTO createMovie(Long categoryId, Movie movie);

    // optional 2: use MovieResponse
    MovieResponse getAllMovies(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    // get one movie
    MovieDTO getMovieById(Long id);

    // get movie by slug
    MovieDTO getMovieBySlug(String slug);

    // update movie
    MovieDTO updateMovie(Long id, Movie movie);

    // delete movie
    String deleteMovie(Long id);

    // get all movies by category
    List<MovieDTO> getMovieByCategory(Category cateID, Movie movie);

    // Add images to movie gallery
    MovieDTO addGalleryImages(Long movieId, List<String> imageUrls);

    // Remove image from movie gallery
    MovieDTO removeGalleryImage(Long movieId, String imageUrl);

    // Update main poster image
    MovieDTO updateMainImage(Long movieId, String newImageUrl);

    // update token when user purchase token to watch ep movie
    // get new movies
    // get the top 10 movies
    // random movie
    // get all movies by year
    // get all movies if movie title contains the search keyword
    // get all movies between the tv/series and Movie/Ova

    // Get overall movie statistics
    MovieStatsDTO getMovieStatistics();

    // Get statistics for a specific category
    MovieStatsDTO getMovieStatisticsByCategory(Long categoryId);

    // Get statistics for a specific time period
    MovieStatsDTO getMovieStatisticsByTimePeriod(String period); // "day", "week", "month", "year"

    // Get top rated movies
    List<MovieDTO> getTopRatedMovies(int limit);

    // Get most viewed movies
    List<MovieDTO> getMostViewedMovies(int limit);

    // Get most commented movies
    List<MovieDTO> getMostCommentedMovies(int limit);

    // Get movies by rating range
    List<MovieDTO> getMoviesByRatingRange(double minRating, double maxRating);

    // Get movies by duration range
    List<MovieDTO> getMoviesByDurationRange(int minDuration, int maxDuration);

    // Search movies by keyword
    MovieResponse searchMovies(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword);
}
