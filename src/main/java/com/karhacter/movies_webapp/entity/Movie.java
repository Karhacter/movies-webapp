package com.karhacter.movies_webapp.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank
    @Size(min = 4, message = "Movie name must contain at least 3 characters")
    @Column(name = "title")
    private String title;
    
    @Column(name = "image")
    private String image;
    
    @NotBlank
    @Size(min = 10, message = "Description must contain at least 10 characters")
    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "release_date")
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    @Min(1)
    @Column(name = "duration")
    private int duration;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    @Column(name = "rating")
    private double rating;
  
    @NotBlank
    @Column(name = "video_url")
    private String videoUrl;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> history;
}