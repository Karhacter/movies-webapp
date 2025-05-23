package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.dto.ReviewDTO;

public interface ReviewService {

    ReviewDTO createReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getReviewsByMovieId(Long movieId);

    ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO);

    void deleteReview(Long reviewId);

    double getAverageRatingByMovieId(Long movieId);

    ReviewDTO createOrUpdateRating(ReviewDTO reviewDTO);

    // New search method
    List<ReviewDTO> searchReviews(String content, String username, String movieTitle, String reviewDate,
            Integer rating);
}
