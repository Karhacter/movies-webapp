package com.karhacter.movies_webapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieStatsDTO {
    private long totalMovies;
    private double averageRating;
    private long totalReviews;
    private long totalComments;
    private long totalViews;
    private long totalTokens;
    private double averageDuration;
    private long moviesByCategory;
    private long moviesThisMonth;
    private long moviesThisYear;
}