package com.karhacter.movies_webapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long id;
    private int season;
    private int episodeNumber;
    private int prriceInTokens;
    private int duration;
    private String videoUrl;
}
