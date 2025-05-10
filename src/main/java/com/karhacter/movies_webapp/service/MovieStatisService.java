package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieStatsDTO;

public interface MovieStatisService {
    // Get overall movie statistics
    MovieStatsDTO getMovieStatistics();

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
}