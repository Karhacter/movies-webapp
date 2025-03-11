package com.karhacter.movies_webapp.payloads;

import java.sql.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private long id;
    private String title;
    private String image;
    private String description;
    private Date releaseDate;
    private int duration;
    private double rating;
    private String videoUrl;
    private Long categoryId; // Thêm categoryId
    private String categoryName; // Thêm categoryName
}
