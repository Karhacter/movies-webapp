package com.karhacter.movies_webapp.payloads;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long id;
    private String title;
    private int season;
    private int episodeNumber;
    private int prriceInTokens;
    private int duration;
    private String downloadUrl;

}
