package com.karhacter.movies_webapp.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private int season;

    @Positive
    private int episodeNumber;

    @Positive
    @Nullable
    private Integer priceInTokens;

    @Positive
    private int duration;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
