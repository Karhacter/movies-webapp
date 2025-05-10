package com.karhacter.movies_webapp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieMapper;
import com.karhacter.movies_webapp.dto.MovieStatsDTO;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.repository.CategoryRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.MovieStatisService;

@Service
public class MovieStatisServiceImpl implements MovieStatisService {

        final private MovieRepo movieRepo;
        final private ModelMapper modelMapper;

        public MovieStatisServiceImpl(MovieRepo movieRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
                this.movieRepo = movieRepo;
                this.modelMapper = modelMapper;
        }

        @Override
        public MovieStatsDTO getMovieStatistics() {
                List<Movie> movies = movieRepo.findAllActive();
                MovieStatsDTO stats = new MovieStatsDTO();

                stats.setTotalMovies(movies.size());
                stats.setAverageRating(movies.stream()
                                .mapToDouble(Movie::getRating)
                                .average()
                                .orElse(0.0));
                stats.setTotalReviews(movies.stream()
                                .mapToLong(m -> m.getReviews().size())
                                .sum());
                stats.setTotalComments(movies.stream()
                                .mapToLong(m -> m.getComments().size())
                                .sum());
                stats.setTotalViews(movies.stream()
                                .mapToLong(m -> m.getHistory().size())
                                .sum());
                stats.setTotalTokens(movies.stream()
                                .mapToInt(Movie::getTokens)
                                .sum());
                stats.setAverageDuration(movies.stream()
                                .mapToInt(Movie::getDuration)
                                .average()
                                .orElse(0.0));

                return stats;
        }

        @Override
        public MovieStatsDTO getMovieStatisticsByTimePeriod(String period) {
                Calendar calendar = Calendar.getInstance();
                Date now = new Date();

                switch (period.toLowerCase()) {
                        case "day":
                                calendar.add(Calendar.DAY_OF_MONTH, -1);
                                break;
                        case "week":
                                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                                break;
                        case "month":
                                calendar.add(Calendar.MONTH, -1);
                                break;
                        case "year":
                                calendar.add(Calendar.YEAR, -1);
                                break;
                        default:
                                throw new IllegalArgumentException("Invalid period: " + period);
                }

                Date startDate = calendar.getTime();
                List<Movie> movies = movieRepo.findAll().stream()
                                .filter(m -> m.getReleaseDate().after(startDate) && m.getReleaseDate().before(now))
                                .collect(Collectors.toList());

                MovieStatsDTO stats = new MovieStatsDTO();
                stats.setMoviesThisMonth(movies.size());
                stats.setAverageRating(movies.stream()
                                .mapToDouble(Movie::getRating)
                                .average()
                                .orElse(0.0));
                stats.setTotalReviews(movies.stream()
                                .mapToLong(m -> m.getReviews().size())
                                .sum());
                stats.setTotalComments(movies.stream()
                                .mapToLong(m -> m.getComments().size())
                                .sum());
                stats.setTotalViews(movies.stream()
                                .mapToLong(m -> m.getHistory().size())
                                .sum());
                stats.setTotalTokens(movies.stream()
                                .mapToInt(Movie::getTokens)
                                .sum());
                stats.setAverageDuration(movies.stream()
                                .mapToInt(Movie::getDuration)
                                .average()
                                .orElse(0.0));

                return stats;
        }

        @Override
        public List<MovieDTO> getTopRatedMovies(int limit) {
                return movieRepo.findAllActive().stream()
                                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                                .limit(limit)
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMostViewedMovies(int limit) {
                return movieRepo.findAllActive().stream()
                                .sorted((m1, m2) -> Long.compare(m2.getHistory().size(), m1.getHistory().size()))
                                .limit(limit)
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMostCommentedMovies(int limit) {
                return movieRepo.findAllActive().stream()
                                .sorted((m1, m2) -> Long.compare(m2.getComments().size(), m1.getComments().size()))
                                .limit(limit)
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMoviesByRatingRange(double minRating, double maxRating) {
                return movieRepo.findAll().stream()
                                .filter(m -> m.getRating() >= minRating && m.getRating() <= maxRating)
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMoviesByDurationRange(int minDuration, int maxDuration) {
                return movieRepo.findAll().stream()
                                .filter(m -> m.getDuration() >= minDuration && m.getDuration() <= maxDuration)
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

}
