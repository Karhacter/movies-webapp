package com.karhacter.movies_webapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long id;
    private Integer season;
    private int episodeNumber;
    private int priceInTokens;
    private int duration;
    private String videoUrl;
    private Long movieId;
    private Long userId;
}
