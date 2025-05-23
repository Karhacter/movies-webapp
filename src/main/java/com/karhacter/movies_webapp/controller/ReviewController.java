package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.ReviewDTO;
import com.karhacter.movies_webapp.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PostMapping("/rate")
    public ResponseEntity<ReviewDTO> createOrUpdateRating(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.createOrUpdateRating(reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovieId(@PathVariable Long movieId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByMovieId(movieId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/movie/{movieId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByMovieId(@PathVariable Long movieId) {
        double averageRating = reviewService.getAverageRatingByMovieId(movieId);
        return new ResponseEntity<>(averageRating, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReviewDTO>> searchReviews(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String movieTitle,
            @RequestParam(required = false) String reviewDate,
            @RequestParam(required = false) Integer rating) {

        List<ReviewDTO> reviews = reviewService.searchReviews(content, username, movieTitle, reviewDate, rating);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
