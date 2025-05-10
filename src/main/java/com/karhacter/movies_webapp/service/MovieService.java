package com.karhacter.movies_webapp.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;

public interface MovieService {

    // add new movie
    MovieDTO createMovie(Movie movie, MultipartFile imageFile);

    // optional 2: use MovieResponse
    Page<MovieDTO> getAllMovies(Pageable pageable);

    Page<MovieDTO> getTrashMovie(Pageable pageable);

    Page<MovieDTO> searchMovies(Pageable pageable, String searchTerm);

    // get one movie
    MovieDTO getMovieById(Long id);

    // get movie by slug
    MovieDTO getMovieBySlug(String slug);

    // get random movie
    MovieDTO getRandomMovie();

    // update movie
    MovieDTO updateMovie(Long id, Movie movie, MultipartFile imageFile);

    // delete movie
    String deleteMovie(Long id);

    // move movie to the trash
    String softDelete(Long id);

    // restore movie
    String restoreMovie(Long id);

    // Get movie by category
    Page<MovieDTO> getMovieByCategory(Category cateID, Pageable pageable);

    // Get seasons by parent movie ID
    List<MovieDTO> getSeasonsByParentId(Long parentId);

    // Get top new movies by category link
    List<MovieDTO> getTopNewByCategoryLink(String categoryLink, int limit);

    // Get top new movies overall
    List<MovieDTO> getTopNewMovies(int limit);

    // MovieDTO updateRating(Long id, Movie movie);
}
