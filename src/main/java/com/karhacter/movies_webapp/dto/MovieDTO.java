package com.karhacter.movies_webapp.dto;

import java.util.Date;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private long id;
    private String title;
    private String image; // Main poster image
    private List<String> galleryImages; // Additional images
    private String description;
    private String slug;
    private Date releaseDate;
    private int duration;
    private double rating;
    private int tokens;
    private String videoUrl;
    private List<CommentDTO> comments;
    private List<ReviewDTO> reviews;
    private List<HistoryDTO> history;
    private Integer statusDelete;
    private Long parentID;
    private Integer seasonNumber;
    private List<CategoryDTO> categories;
}
