package com.karhacter.movies_webapp.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.MovieDTO;
import com.karhacter.movies_webapp.payloads.MovieStatsDTO;
import com.karhacter.movies_webapp.repository.CategoryRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

        final private MovieRepo movieRepo;
        final private CategoryRepo categoryRepo;
        final private ModelMapper modelMapper;
        private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

        public MovieServiceImpl(MovieRepo movieRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
                this.movieRepo = movieRepo;
                this.categoryRepo = categoryRepo;
                this.modelMapper = modelMapper;
        }

        @Override
        public MovieDTO createMovie(Long categoryId, Movie movie) {
                Category category = categoryRepo.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
                movie.setCategory(category);
                Movie savedMovie = movieRepo.save(movie);
                return modelMapper.map(savedMovie, MovieDTO.class);
        }

        @Override
        public Page<MovieDTO> getAllMovies(Pageable pageable) {
                logger.info("Service: Getting movies for page: {}, size: {}", pageable.getPageNumber(),
                                pageable.getPageSize());
                Page<Movie> movies = movieRepo.findAll(pageable);
                Page<MovieDTO> movieDTOs = movies.map(movie -> modelMapper.map(movie, MovieDTO.class));
                logger.info("Service: Returning {} movies for page: {}", movieDTOs.getContent().size(),
                                movieDTOs.getNumber());
                return movieDTOs;
        }

        @Override
        public MovieDTO getMovieById(Long id) {
                Movie movie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
                return modelMapper.map(movie, MovieDTO.class);
        }

        @Override
        public MovieDTO getMovieBySlug(String slug) {
                Movie movie = movieRepo.findBySlug(slug)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "slug", slug));
                return modelMapper.map(movie, MovieDTO.class);
        }

        @Override
        public MovieDTO updateMovie(Long id, Movie movie) {
                Movie existingMovie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

                // Update fields
                existingMovie.setTitle(movie.getTitle());
                existingMovie.setDescription(movie.getDescription());
                existingMovie.setDuration(movie.getDuration());
                existingMovie.setRating(movie.getRating());
                existingMovie.setTokens(movie.getTokens());
                existingMovie.setVideoUrl(movie.getVideoUrl());
                existingMovie.setCategory(movie.getCategory());

                Movie updatedMovie = movieRepo.save(existingMovie);
                return modelMapper.map(updatedMovie, MovieDTO.class);
        }

        @Override
        public String deleteMovie(Long id) {
                Movie movie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
                movieRepo.delete(movie);
                return "Movie deleted successfully";
        }

        @Override
        public List<MovieDTO> getMovieByCategory(Category cateID, Movie movie) {
                List<Movie> movies = movieRepo.findByCategory(cateID);
                return movies.stream()
                                .map(m -> modelMapper.map(m, MovieDTO.class))
                                .collect(Collectors.toList());
        }

        @Override
        public MovieDTO addGalleryImages(Long movieId, List<String> imageUrls) {
                Movie movie = movieRepo.findById(movieId)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

                if (movie.getGalleryImages() == null) {
                        movie.setGalleryImages(imageUrls);
                } else {
                        movie.getGalleryImages().addAll(imageUrls);
                }

                Movie updatedMovie = movieRepo.save(movie);
                return modelMapper.map(updatedMovie, MovieDTO.class);
        }

        @Override
        public MovieDTO removeGalleryImage(Long movieId, String imageUrl) {
                Movie movie = movieRepo.findById(movieId)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

                if (movie.getGalleryImages() != null) {
                        movie.getGalleryImages().remove(imageUrl);
                }

                Movie updatedMovie = movieRepo.save(movie);
                return modelMapper.map(updatedMovie, MovieDTO.class);
        }

        @Override
        public MovieDTO updateMainImage(Long movieId, String newImageUrl) {
                Movie movie = movieRepo.findById(movieId)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

                movie.setImage(newImageUrl);
                Movie updatedMovie = movieRepo.save(movie);
                return modelMapper.map(updatedMovie, MovieDTO.class);
        }

        @Override
        public MovieStatsDTO getMovieStatistics() {
                List<Movie> movies = movieRepo.findAll();
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
        public MovieStatsDTO getMovieStatisticsByCategory(Long categoryId) {
                Category category = categoryRepo.findById(categoryId)
                                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
                List<Movie> movies = movieRepo.findByCategory(category);

                MovieStatsDTO stats = new MovieStatsDTO();
                stats.setMoviesByCategory(movies.size());
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
                return movieRepo.findAll().stream()
                                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                                .limit(limit)
                                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMostViewedMovies(int limit) {
                return movieRepo.findAll().stream()
                                .sorted((m1, m2) -> Long.compare(m2.getHistory().size(), m1.getHistory().size()))
                                .limit(limit)
                                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMostCommentedMovies(int limit) {
                return movieRepo.findAll().stream()
                                .sorted((m1, m2) -> Long.compare(m2.getComments().size(), m1.getComments().size()))
                                .limit(limit)
                                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMoviesByRatingRange(double minRating, double maxRating) {
                return movieRepo.findAll().stream()
                                .filter(m -> m.getRating() >= minRating && m.getRating() <= maxRating)
                                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getMoviesByDurationRange(int minDuration, int maxDuration) {
                return movieRepo.findAll().stream()
                                .filter(m -> m.getDuration() >= minDuration && m.getDuration() <= maxDuration)
                                .map(movie -> modelMapper.map(movie, MovieDTO.class))
                                .collect(Collectors.toList());
        }
}