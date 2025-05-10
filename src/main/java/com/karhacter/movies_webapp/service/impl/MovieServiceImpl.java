package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieMapper;
import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.repository.CategoryRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.MovieService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl implements MovieService {

        final private MovieRepo movieRepo;
        final private CategoryRepo categoryRepo;
        final private ModelMapper modelMapper;
        private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

        private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        public MovieServiceImpl(MovieRepo movieRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
                this.movieRepo = movieRepo;
                this.categoryRepo = categoryRepo;
                this.modelMapper = modelMapper;

                try {
                        Files.createDirectories(this.fileStorageLocation);
                } catch (Exception ex) {
                        throw new RuntimeException(
                                        "Could not create the directory where the uploaded files will be stored.", ex);
                }
        }

        @Override
        public MovieDTO getRandomMovie() {
                Movie movie = movieRepo.findRandomMovie();
                if (movie == null) {
                        throw new ResourceNotFoundException("Movie", "random", "No movies found");
                }
                return MovieMapper.convertToDTO(movie);
        }

        // Helper method to get movies sorted by release date descending and limited by
        // count
        private List<MovieDTO> getTopNewMoviesFromList(List<Movie> movies, int limit) {
                return movies.stream()
                                .sorted((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()))
                                .limit(limit)
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

        // Helper method to get movies by category with sorting and limit
        private List<MovieDTO> getTopNewMoviesByCategory(Category category, int limit) {
                Pageable pageable = PageRequest.of(0, limit, Sort.by("releaseDate").descending());
                Page<Movie> moviePage = movieRepo.findByCategory(category, pageable);
                return moviePage.stream()
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<MovieDTO> getTopNewByCategoryLink(String categoryLink, int limit) {
                Category category = categoryRepo.findByLink(categoryLink);
                if (category == null) {
                        throw new ResourceNotFoundException("Category", "link", categoryLink);
                }
                return getTopNewMoviesByCategory(category, limit);
        }

        // Helper method to get movies by category with sorting and limit

        @Override
        public List<MovieDTO> getTopNewMovies(int limit) {
                List<Movie> movies = movieRepo.findAllActive();
                return getTopNewMoviesFromList(movies, limit);
        }

        @Override
        public List<MovieDTO> getSeasonsByParentId(Long parentId) {
                if (!movieRepo.existsById(parentId)) {
                        throw new ResourceNotFoundException("Movie", "id", parentId);
                }
                return movieRepo.findSeasonsByParentId(parentId).stream()
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());

        }

        private String convertTitleToSlug(String title) {
                if (title == null) {
                        return "";
                }
                // Convert to lowercase
                String slug = title.toLowerCase();
                // Replace spaces and special characters with hyphens
                slug = slug.replaceAll("[\\s\\p{Punct}]+", "-");
                // Remove non-alphanumeric characters except hyphens
                slug = slug.replaceAll("[^a-z0-9-]", "");
                // Trim leading and trailing hyphens
                slug = slug.replaceAll("^-+|-+$", "");
                return slug;
        }

        @Override
        public MovieDTO createMovie(Movie movie, MultipartFile imageFile) {
                List<Category> categories = movie.getCategories();
                if (categories != null && !categories.isEmpty()) {
                        List<Category> managedCategories = categories.stream()
                                        .map(cat -> categoryRepo.findById(cat.getId())
                                                        .orElseThrow(() -> new ResourceNotFoundException("Category",
                                                                        "id", Long.valueOf(cat.getId()))))
                                        .collect(Collectors.toList());
                        movie.setCategories(managedCategories);
                } else {
                        throw new ResourceNotFoundException("Category", "id", "null");
                }

                // Set slug from title
                movie.setSlug(convertTitleToSlug(movie.getTitle()));

                if (imageFile != null && !imageFile.isEmpty()) {
                        String imageUrl = uploadImage(imageFile);
                        movie.setImage(imageUrl);
                }

                // Assign seasonNumber based on parentID and if parentID equals movie's own ID
                if (movie.getParentID() == null || movie.getParentID().equals(movie.getId())) {
                        movie.setSeasonNumber(1);
                } else {
                        List<Movie> siblings = movieRepo.findByParentID(movie.getParentID());
                        int maxSeason = siblings.stream()
                                        .mapToInt(m -> m.getSeasonNumber() != null ? m.getSeasonNumber() : 0)
                                        .max()
                                        .orElse(0);
                        movie.setSeasonNumber(maxSeason + 1);
                }

                Movie savedMovie = movieRepo.save(movie);
                return modelMapper.map(savedMovie, MovieDTO.class);
        }

        public String uploadImage(MultipartFile file) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                try {
                        Path targetLocation = this.fileStorageLocation.resolve(fileName);
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                        // Return the relative path or URL to access the image
                        return "/uploads/" + fileName;
                } catch (Exception ex) {
                        throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
                }
        }

        @Override
        public Page<MovieDTO> getTrashMovie(Pageable pageable) {
                logger.info("Fetching deleted movies for page {}", pageable.getPageNumber());

                // Option 1: Proper JPA pagination (recommended)
                Page<Movie> moviePage = movieRepo.findByStatusDelete(0, pageable); // Assuming repository supports this

                // Convert using MovieMapper
                return moviePage.map(MovieMapper::convertToDTO);
        }

        @Override
        public Page<MovieDTO> getAllMovies(Pageable pageable) {
                logger.info("Service: Getting movies for page: {}, size: {}",
                                pageable.getPageNumber(), pageable.getPageSize());

                // Fetch paginated data from DB (proper JPA pagination)
                Page<Movie> moviePage = movieRepo.findAll(pageable);

                // Convert entities to DTOs using your existing MovieMapper
                List<MovieDTO> movieDTOs = moviePage.getContent().stream()
                                .map(MovieMapper::convertToDTO) // Uses manual conversion below
                                .collect(Collectors.toList());

                logger.info("Service: Returning {} movies for page: {}",
                                movieDTOs.size(), pageable.getPageNumber());

                return new PageImpl<>(movieDTOs, pageable, moviePage.getTotalElements());
        }

        // Searching Movie
        @Override
        public Page<MovieDTO> searchMovies(Pageable pageable, String searchTerm) {
                logger.info("Service: Getting movies for page: {}, size: {}",
                                pageable.getPageNumber(), pageable.getPageSize());

                Page<Movie> moviePage = movieRepo.searchMovies(searchTerm, pageable);

                List<MovieDTO> movieDTOs = moviePage.getContent().stream()
                                .map(MovieMapper::convertToDTO)
                                .collect(Collectors.toList());
                logger.info("Service: Returning {} movies for page: {}",
                                movieDTOs.size(), pageable.getPageNumber());
                return new PageImpl<>(movieDTOs, pageable, moviePage.getTotalElements());
        }

        @Override
        public MovieDTO getMovieById(Long id) {
                Movie movie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
                return MovieMapper.convertToDTO(movie);
        }

        @Override
        public MovieDTO getMovieBySlug(String slug) {
                Movie movie = movieRepo.findBySlug(slug)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "slug", slug));
                return MovieMapper.convertToDTO(movie);
        }

        @Override
        public MovieDTO updateMovie(Long id, Movie movie, MultipartFile imageFile) {
                Movie existingMovie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

                // Update fields
                existingMovie.setTitle(movie.getTitle());
                existingMovie.setDescription(movie.getDescription());
                existingMovie.setDuration(movie.getDuration());
                existingMovie.setRating(movie.getRating());
                existingMovie.setTokens(movie.getTokens());

                // Assign seasonNumber based on parentID
                if (movie.getParentID() == null) {
                        existingMovie.setSeasonNumber(1);
                } else {
                        List<Movie> siblings = movieRepo.findByParentID(movie.getParentID());
                        int maxSeason = siblings.stream()
                                        .mapToInt(m -> m.getSeasonNumber() != null ? m.getSeasonNumber() : 0)
                                        .max()
                                        .orElse(0);
                        existingMovie.setSeasonNumber(maxSeason + 1);
                }

                List<Category> categories = movie.getCategories();
                if (categories != null && !categories.isEmpty()) {
                        List<Category> managedCategories = categories.stream()
                                        .map(cat -> categoryRepo.findById(cat.getId())
                                                        .orElseThrow(() -> new ResourceNotFoundException("Category",
                                                                        "id", Long.valueOf(cat.getId()))))
                                        .collect(Collectors.toList());
                        existingMovie.setCategories(managedCategories);
                } else {
                        throw new ResourceNotFoundException("Category", "id", "null");
                }

                // Update slug from title
                existingMovie.setSlug(convertTitleToSlug(movie.getTitle()));

                if (imageFile != null && !imageFile.isEmpty()) {
                        String imageUrl = uploadImage(imageFile);
                        existingMovie.setImage(imageUrl);
                }

                Movie updatedMovie = movieRepo.save(existingMovie);
                return MovieMapper.convertToDTO(updatedMovie);
        }

        @Override
        public String deleteMovie(Long id) {
                Movie movie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
                // Soft delete: set statusDelete to 0
                movieRepo.delete(movie);
                return "Movie deleted successfully";
        }

        // action for restore the movie
        @Override
        @Transactional
        public String restoreMovie(Long id) {
                Movie movie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
                // Restore: set statusDelete to 1
                movie.setStatusDelete(1);
                movieRepo.save(movie);
                return "Movie restored successfully";
        }

        // action for soft delete movie
        @Override
        @Transactional
        public String softDelete(Long id) {
                Movie movie = movieRepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
                // Restore: set statusDelete to 1
                movie.setStatusDelete(0);
                movieRepo.save(movie);
                return "Movie soft-deleted successfully";
        }

        @Override
        public Page<MovieDTO> getMovieByCategory(Category cateID, Pageable pageable) {
                Page<Movie> moviePage = movieRepo.findByCategory(cateID, pageable);

                return moviePage.map(MovieMapper::convertToDTO);
        }

}