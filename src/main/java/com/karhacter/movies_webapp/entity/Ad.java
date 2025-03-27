package com.karhacter.movies_webapp.entity;

import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ads")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@Setter
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String targetUrl;

    @Enumerated(EnumType.STRING)
    private AdType type; // BANNER, POPUP, VIDEO, NATIVE

    @Enumerated(EnumType.STRING)
    private AdStatus status; // ACTIVE, INACTIVE, PENDING, EXPIRED

    @DecimalMin("0.0")
    private double budget;

    @DecimalMin("0.0")
    private double spent;

    @Min(0)
    private int impressions;

    @Min(0)
    private int clicks;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "advertiser_id")
    private User advertiser;

    @PrePersist
    protected void onCreate() {
        startDate = new Date();
        status = AdStatus.PENDING;
        spent = 0.0;
        impressions = 0;
        clicks = 0;
    }
}