package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.Review;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.ReviewDTO;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.repository.ReviewRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        // Get user and movie
        User user = userRepo.findById(reviewDTO.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", reviewDTO.getUserID()));
        Movie movie = movieRepo.findById(reviewDTO.getMovieID())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", reviewDTO.getMovieID()));

        // Check if user has already reviewed this movie
        if (reviewRepo.existsByUserAndMovie(user, movie)) {
            throw new APIException("You have already reviewed this movie!");
        }

        // Create and save review
        Review review = modelMapper.map(reviewDTO, Review.class);
        review.setUser(user);
        review.setMovie(movie);
        review = reviewRepo.save(review);

        // Update movie's average rating
        updateMovieRating(movie);

        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public List<ReviewDTO> getReviewsByMovieId(Long movieId) {
        List<Review> reviews = reviewRepo.findByMovieId(movieId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        // Get existing review
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        // Verify user owns this review
        if (review.getUser().getUserID() != reviewDTO.getUserID()) {
            throw new APIException("You can only update your own reviews!");
        }

        // Update review
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review = reviewRepo.save(review);

        // Update movie's average rating
        updateMovieRating(review.getMovie());

        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        Movie movie = review.getMovie();
        reviewRepo.delete(review);

        // Update movie's average rating
        updateMovieRating(movie);
    }

    private void updateMovieRating(Movie movie) {
        List<Review> reviews = reviewRepo.findByMovieId(movie.getId());
        if (!reviews.isEmpty()) {
            double averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            movie.setRating(averageRating);
            movieRepo.save(movie);
        }
    }
}