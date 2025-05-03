package com.karhacter.movies_webapp.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieStatsDTO;
import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;

public interface MovieService {

    // add new movie
    MovieDTO createMovie(Movie movie, MultipartFile imageFile);

    // optional 2: use MovieResponse

    Page<MovieDTO> getAllMovies(Pageable pageable);

    Page<MovieDTO> getTrashMovie(Pageable pageable);

    // get one movie
    MovieDTO getMovieById(Long id);

    // get movie by slug
    MovieDTO getMovieBySlug(String slug);

    // update movie
    MovieDTO updateMovie(Long id, Movie movie, MultipartFile imageFile);

    // delete movie
    String deleteMovie(Long id);

    String softDelete(Long id);

    // restore movie
    String restoreMovie(Long id);

    // get all movies by category
    Page<MovieDTO> getMovieByCategory(Category cateID, Movie movie, Pageable pageable);

    // Add images to movie gallery
    MovieDTO addGalleryImages(Long movieId, List<String> imageUrls);

    // Remove image from movie gallery
    MovieDTO removeGalleryImage(Long movieId, String imageUrl);

    // Update main poster image
    MovieDTO updateMainImage(Long movieId, String newImageUrl);

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

    // Get seasons by parent movie ID
    List<MovieDTO> getSeasonsByParentId(Long parentId);

}
