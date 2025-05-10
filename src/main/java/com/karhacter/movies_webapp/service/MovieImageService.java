package com.karhacter.movies_webapp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.karhacter.movies_webapp.dto.MovieDTO;

public interface MovieImageService {

    // Add images to movie gallery
    MovieDTO addGalleryImages(Long movieId, List<MultipartFile> imageUrls);

    // Remove image from movie gallery
    MovieDTO removeGalleryImage(Long movieId, String imageUrl);

    // Update main poster image
    MovieDTO updateMainImage(Long movieId, String imageUrl);
}
