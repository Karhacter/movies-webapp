package com.karhacter.movies_webapp.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.karhacter.movies_webapp.entity.Category;
import com.karhacter.movies_webapp.entity.Movie;

public class MovieMapper {

    public static Movie convertToEntity(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setImage(movieDTO.getImage());
        movie.setGalleryImages(movieDTO.getGalleryImages());
        movie.setDescription(movieDTO.getDescription());
        movie.setSlug(movieDTO.getSlug());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setDuration(movieDTO.getDuration());
        movie.setRating(movieDTO.getRating());
        movie.setTokens(movieDTO.getTokens());
        movie.setVideoUrl(movieDTO.getVideoUrl());
        movie.setStatusDelete(movieDTO.getStatusDelete() != null ? movieDTO.getStatusDelete() : 1);
        movie.setSeasonNumber(movieDTO.getSeasonNumber());

        // Convert categories from CategoryDTO to Category entities
        if (movieDTO.getCategories() != null && !movieDTO.getCategories().isEmpty()) {
            List<Category> categories = movieDTO.getCategories().stream()
                    .map(catDTO -> {
                        Category category = new Category();
                        category.setId(catDTO.getId());
                        return category;
                    })
                    .collect(Collectors.toList());
            movie.setCategories(categories);
        } else {
            movie.setCategories(null);
        }

        // Resolve parentID Long to Movie entity
        if (movieDTO.getParentID() != null) {
            Movie parentMovie = new Movie();
            parentMovie.setId(movieDTO.getParentID());
            movie.setParentID(parentMovie);
        } else {
            movie.setParentID(null);
        }

        return movie;
    }
}
