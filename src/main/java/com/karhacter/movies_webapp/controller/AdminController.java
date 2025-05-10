package com.karhacter.movies_webapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.karhacter.movies_webapp.dto.AdDTO;
import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.PaymentDTO;
import com.karhacter.movies_webapp.dto.ReviewDTO;
import com.karhacter.movies_webapp.dto.UserDTO;
import com.karhacter.movies_webapp.service.AdService;
import com.karhacter.movies_webapp.service.MovieService;
import com.karhacter.movies_webapp.service.PaymentService;
import com.karhacter.movies_webapp.service.ReviewService;
import com.karhacter.movies_webapp.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AdService adService;

    @Autowired
    private PaymentService paymentService;

    // Movie search
    @GetMapping("/movies/search")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String releaseDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        String searchTerm = title;
        // For simplicity, combining genre and releaseDate into searchTerm or extend
        // service method later

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MovieDTO> result = movieService.searchMovies(pageable, searchTerm);
        return ResponseEntity.ok(result);
    }

    // User search
    @GetMapping("/users/search")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @RequestParam String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        // To be implemented in service layer
        return ResponseEntity.ok(userService.getPageUser(PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending())));
    }

    // Review search
    @GetMapping("/reviews/search")
    public ResponseEntity<List<ReviewDTO>> searchReviews(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String movieTitle,
            @RequestParam(required = false) String reviewDate,
            @RequestParam(required = false) Integer rating) {

        // To be implemented in service layer
        return ResponseEntity.ok(reviewService.getReviewsByMovieId(null)); // Placeholder
    }

    // Ad search
    @GetMapping("/ads/search")
    public ResponseEntity<List<AdDTO>> searchAds(
            @RequestParam(required = false) String adName,
            @RequestParam(required = false) String campaignOwner,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String status) {

        // To be implemented in service layer
        return ResponseEntity.ok(adService.getAllActiveAds()); // Placeholder
    }

    // Payment search
    @GetMapping("/payments/search")
    public ResponseEntity<Page<PaymentDTO>> searchPayments(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        // To be implemented in service layer
        return ResponseEntity.ok(paymentService.getPagePayments(PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending())));
    }
}
