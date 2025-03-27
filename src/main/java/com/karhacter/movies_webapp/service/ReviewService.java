package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.payloads.ReviewDTO;

public interface ReviewService {
    ReviewDTO createReview(ReviewDTO reviewDTO);
    List<ReviewDTO> getReviewsByMovieId(Long movieId);
    ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO);
    void deleteReview(Long reviewId);
}
