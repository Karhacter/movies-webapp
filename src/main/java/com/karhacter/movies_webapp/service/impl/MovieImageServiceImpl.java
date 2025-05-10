package com.karhacter.movies_webapp.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieMapper;
import com.karhacter.movies_webapp.exception.BadRequestException;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.repository.CategoryRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.MovieImageService;

import jakarta.transaction.Transactional;

@Service
public class MovieImageServiceImpl implements MovieImageService {

    final private MovieRepo movieRepo;
    final private ModelMapper modelMapper;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    private static final Logger logger = LoggerFactory.getLogger(MovieImageServiceImpl.class);

    public MovieImageServiceImpl(MovieRepo movieRepo, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.movieRepo = movieRepo;
        this.modelMapper = modelMapper;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String uploadImage(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            // Return the relative path or URL to access the image
            return "/uploads/" + fileName;
        } catch (Exception ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public MovieDTO addGalleryImages(Long movieId, List<MultipartFile> imageFiles) {
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

        List<String> imageUrls = new ArrayList<>();
        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (file != null && !file.isEmpty()) {
                    String imageUrl = uploadImage(file);
                    imageUrls.add(imageUrl);
                }
            }
        }

        if (movie.getGalleryImages() == null) {
            movie.setGalleryImages(new ArrayList<>());
        }
        movie.getGalleryImages().addAll(imageUrls);

        Movie updatedMovie = movieRepo.save(movie);
        return MovieMapper.convertToDTO(updatedMovie);
    }

    @Override
    @Transactional
    public MovieDTO removeGalleryImage(Long movieId, String imageUrl) {
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

        if (movie.getGalleryImages() == null || !movie.getGalleryImages().contains(imageUrl)) {
            throw new ResourceNotFoundException("Gallery image", "url", imageUrl);
        }

        // Remove from database
        movie.getGalleryImages().remove(imageUrl);
        Movie updatedMovie = movieRepo.save(movie);

        // Delete actual file
        try {
            Path filePath = this.fileStorageLocation.resolve(extractFilenameFromUrl(imageUrl));
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Failed to delete image file: {}", imageUrl, e);
            // Continue even if file deletion fails
        }

        return MovieMapper.convertToDTO(updatedMovie);
    }

    private String extractFilenameFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
    }

    @Override
    @Transactional
    public MovieDTO updateMainImage(Long movieId, String imageUrl) {
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new BadRequestException("Image URL cannot be empty");
        }

        // Check if the imageUrl exists in the gallery images
        if (movie.getGalleryImages() == null || !movie.getGalleryImages().contains(imageUrl)) {
            throw new BadRequestException("Hình Ảnh Được Chọn Không Nằm Trong Kho Ảnh");
        }

        // Set the main image to the selected gallery image
        movie.setImage(imageUrl);

        Movie updatedMovie = movieRepo.save(movie);
        return MovieMapper.convertToDTO(updatedMovie);
    }
}
