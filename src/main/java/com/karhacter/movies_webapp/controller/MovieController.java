package com.karhacter.movies_webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieMapper;
import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.repository.CategoryRepo;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.service.MovieService;

@RestController
@RequestMapping("/api/")
@CrossOrigin
public class MovieController {
        private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

        @Autowired
        private CategoryRepo categoryRepo;

        @Autowired
        private MovieService movieService;

        public MovieController(MovieService movieService, CategoryRepo categoryRepo) {
                this.movieService = movieService;
                this.categoryRepo = categoryRepo;
        }

        // New endpoint: get random movie detail
        @GetMapping("movie/random")
        public ResponseEntity<MovieDTO> getRandomMovie() {
                MovieDTO movieDTO = movieService.getRandomMovie();
                return new ResponseEntity<>(movieDTO, HttpStatus.OK);
        }

        @PostMapping(value = "movies/add", consumes = { "multipart/form-data" })
        public ResponseEntity<MovieDTO> createMovie(
                        @RequestPart("movie") String movieJson,
                        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
                ObjectMapper objectMapper = new ObjectMapper();
                MovieDTO movieDTO;
                try {
                        movieDTO = objectMapper.readValue(movieJson, MovieDTO.class);
                } catch (Exception e) {
                        return ResponseEntity.badRequest().build();
                }
                Movie movie = MovieMapper.convertToEntity(movieDTO);
                MovieDTO savedMovieDTO = movieService.createMovie(movie, imageFile);
                return new ResponseEntity<>(savedMovieDTO, HttpStatus.CREATED);
        }

        // get all movies list in admin mode

        @GetMapping("movies/trash")
        public ResponseEntity<Page<MovieDTO>> getTrashMovie(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "rating") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortOrder) {

                logger.info("Received request for movies - Page: {}, Size: {}, SortBy: {}, SortOrder: {}",
                                page, size, sortBy, sortOrder);

                // Ensure page is not negative
                page = Math.max(0, page);

                Sort sort = sortOrder.equalsIgnoreCase("desc")
                                ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<MovieDTO> movies = movieService.getTrashMovie(pageable);

                logger.info("Returning movies - Current Page: {}, Total Pages: {}, Total Elements: {}",
                                movies.getNumber(), movies.getTotalPages(), movies.getTotalElements());

                return new ResponseEntity<>(movies, HttpStatus.OK);
        }

        // get all movies
        @GetMapping("movies/index")
        public ResponseEntity<Page<MovieDTO>> getAllMovies(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "rating") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortOrder) {

                logger.info("Received request for movies - Page: {}, Size: {}, SortBy: {}, SortOrder: {}",
                                page, size, sortBy, sortOrder);

                // Ensure page is not negative
                page = Math.max(0, page);

                Sort sort = sortOrder.equalsIgnoreCase("desc")
                                ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);
                Page<MovieDTO> movies = movieService.getAllMovies(pageable);

                logger.info("Returning movies - Current Page: {}, Total Pages: {}, Total Elements: {}",
                                movies.getNumber(), movies.getTotalPages(), movies.getTotalElements());

                return new ResponseEntity<>(movies, HttpStatus.OK);
        }

        // get one movie
        @GetMapping("movie/detail/{id}")
        public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
                MovieDTO detailMovieDTO = movieService.getMovieById(id);
                return new ResponseEntity<>(detailMovieDTO, HttpStatus.OK);
        }

        // update movie
        @PutMapping(value = "movie/update/{id}", consumes = { "multipart/form-data" })
        public ResponseEntity<MovieDTO> updateMovie(
                        @PathVariable Long id,
                        @RequestPart("movie") String movieJson,
                        @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
                ObjectMapper objectMapper = new ObjectMapper();
                MovieDTO movieDTO;
                try {
                        movieDTO = objectMapper.readValue(movieJson, MovieDTO.class);
                } catch (Exception e) {
                        return ResponseEntity.badRequest().build();
                }
                Movie movie = MovieMapper.convertToEntity(movieDTO);
                MovieDTO updatedMovieDTO = movieService.updateMovie(id, movie, imageFile);
                return new ResponseEntity<>(updatedMovieDTO, HttpStatus.OK);
        }

        // delete movie (delete force)
        @DeleteMapping("movie/delete/{id}")
        public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
                String message = movieService.deleteMovie(id);
                return new ResponseEntity<>(message, HttpStatus.OK);
        }

        // restore movie
        @PostMapping("movie/restore/{id}")
        public ResponseEntity<String> restoreMovie(@PathVariable Long id) {
                String message = movieService.restoreMovie(id);
                return new ResponseEntity<>(message, HttpStatus.OK);
        }

        // soft delete
        @PostMapping("movie/soft-delete/{id}")
        public ResponseEntity<String> softDelete(@PathVariable Long id) {
                String message = movieService.softDelete(id);
                return new ResponseEntity<>(message, HttpStatus.OK);
        }

        // get movie by slug
        @GetMapping("movie/slug/{slug}")
        public ResponseEntity<MovieDTO> getMovieBySlug(@PathVariable String slug) {
                MovieDTO movieDTO = movieService.getMovieBySlug(slug);
                return new ResponseEntity<>(movieDTO, HttpStatus.OK);
        }

        // get movies by category link (slug)
        @GetMapping("movies/category/link/{categoryLink:.+}")
        public ResponseEntity<Page<MovieDTO>> getMoviesByCategoryLink(
                        @PathVariable String categoryLink,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                String decodedLink = java.net.URLDecoder.decode(categoryLink, java.nio.charset.StandardCharsets.UTF_8);
                logger.info("Received request for movies by category - CategoryLink: {}, Page: {}, Size: {}",
                                decodedLink, page, size);

                page = Math.max(0, page);
                Pageable pageable = PageRequest.of(page, size);
                Category category = categoryRepo.findByLink(decodedLink);
                if (category == null) {
                        throw new ResourceNotFoundException("Category", "link", decodedLink);
                }
                Page<MovieDTO> movies = movieService.getMovieByCategory(category, pageable);

                logger.info("Returning movies for category {} - Current Page: {}, Total Pages: {}, Total Elements: {}",
                                decodedLink, movies.getNumber(), movies.getTotalPages(), movies.getTotalElements());

                return new ResponseEntity<>(movies, HttpStatus.OK);
        }

        @GetMapping("movies/{parentId}/seasons")
        public ResponseEntity<List<MovieDTO>> getSeasonsByParentId(@PathVariable Long parentId) {
                List<MovieDTO> seasons = movieService.getSeasonsByParentId(parentId);
                return new ResponseEntity<>(seasons, HttpStatus.OK);
        }

        // Searching Movies
        @GetMapping("/movies/search")
        public ResponseEntity<Page<MovieDTO>> searchMovies(
                        @RequestParam String search,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "title") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortOrder) {

                logger.info("Received search request - Query: '{}', Page: {}, Size: {}, SortBy: {}, SortOrder: {}",
                                search, page, size, sortBy, sortOrder);

                page = Math.max(0, page);

                Sort sort = sortOrder.equalsIgnoreCase("desc")
                                ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);

                Page<MovieDTO> result = movieService.searchMovies(pageable, search);

                logger.info("Search result - Current Page: {}, Total Pages: {}, Total Elements: {}",
                                result.getNumber(), result.getTotalPages(), result.getTotalElements());

                return ResponseEntity.ok(result);
        }

        // New endpoint: Get top new movies by category link with limit
        @GetMapping("movies/top-new/category/{categoryLink:.+}")
        public ResponseEntity<List<MovieDTO>> getTopNewByCategoryLink(
                        @PathVariable String categoryLink,
                        @RequestParam(defaultValue = "10") int limit) {

                String decodedLink = java.net.URLDecoder.decode(categoryLink, java.nio.charset.StandardCharsets.UTF_8);
                logger.info("Received request for top new movies by category - CategoryLink: {}, Limit: {}",
                                decodedLink, limit);

                List<MovieDTO> movies = movieService.getTopNewByCategoryLink(decodedLink, limit);

                logger.info("Returning top new movies for category {} - Count: {}", decodedLink, movies.size());

                return new ResponseEntity<>(movies, HttpStatus.OK);
        }

        // New endpoint: Get top new movies overall with limit
        @GetMapping("movies/top-new")
        public ResponseEntity<List<MovieDTO>> getTopNewMovies(
                        @RequestParam(defaultValue = "10") int limit) {

                logger.info("Received request for top new movies overall - Limit: {}", limit);

                List<MovieDTO> movies = movieService.getTopNewMovies(limit);

                logger.info("Returning top new movies overall - Count: {}", movies.size());

                return new ResponseEntity<>(movies, HttpStatus.OK);
        }
}
