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

    public static MovieDTO convertToDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setImage(movie.getImage());
        dto.setGalleryImages(movie.getGalleryImages());
        dto.setDescription(movie.getDescription());
        dto.setSlug(movie.getSlug());
        dto.setDuration(movie.getDuration());
        dto.setRating(movie.getRating());
        dto.setTokens(movie.getTokens());
        dto.setStatusDelete(movie.getStatusDelete());
        dto.setParentID(movie.getParentID() != null ? movie.getParentID().getId() : null);
        dto.setSeasonNumber(movie.getSeasonNumber());

        // Handle release date conversion
        if (movie.getReleaseDate() != null) {
            dto.setReleaseDate(new java.sql.Date(movie.getReleaseDate().getTime()));
        }
        // Convert Categories properly
        if (movie.getCategories() != null) {
            dto.setCategories(movie.getCategories().stream()
                    .map(cat -> {
                        CategoryDTO categoryDTO = new CategoryDTO();
                        categoryDTO.setId(cat.getId());
                        categoryDTO.setName(cat.getName());
                        categoryDTO.setLink(cat.getLink()); // Add this if needed
                        return categoryDTO;
                    })
                    .collect(Collectors.toList()));
        }

        // Handle lazy-loaded collections
        if (movie.getComments() != null) {
            dto.setComments(movie.getComments().stream()
                    .map(comment -> {
                        CommentDTO commentDTO = new CommentDTO();
                        commentDTO.setId(comment.getId());
                        commentDTO.setContent(comment.getContent());
                        commentDTO.setCreatedAt(comment.getCreatedAt());
                        commentDTO.setUserId(comment.getUser() != null ? comment.getUser().getUserID() : null);
                        return commentDTO;
                    })
                    .collect(Collectors.toList()));
        } else {
            dto.setComments(null);
        }

        if (movie.getReviews() != null) {
            dto.setReviews(movie.getReviews().stream()
                    .map(review -> {
                        ReviewDTO reviewDTO = new ReviewDTO();
                        reviewDTO.setId(review.getId());
                        reviewDTO.setRating(review.getRating());
                        reviewDTO.setComment(review.getComment());
                        reviewDTO.setUserID(review.getUser() != null ? review.getUser().getUserID() : null);
                        return reviewDTO;
                    })
                    .collect(Collectors.toList()));
        } else {
            dto.setReviews(null);
        }

        if (movie.getHistory() != null) {
            dto.setHistory(movie.getHistory().stream()
                    .map(history -> {
                        HistoryDTO historyDTO = new HistoryDTO();
                        historyDTO.setId(history.getId());
                        historyDTO.setLastWatched(history.getLastWatched());
                        historyDTO.setUserId(history.getUser() != null ? history.getUser().getUserID() : null);
                        return historyDTO;
                    })
                    .collect(Collectors.toList()));
        } else {
            dto.setHistory(null);
        }

        return dto;
    }
}
