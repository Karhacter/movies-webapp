package com.karhacter.movies_webapp.payloads;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private long id;
    private double rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long userID;
    private Long movieID;
}
