package com.karhacter.movies_webapp.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class VideoStreamController {

    private static final String VIDEO_DIR = System.getProperty("user.dir") + "/video";

    @GetMapping("/api/video/{filename:.+}")
    public ResponseEntity<InputStreamResource> streamVideo(
            @PathVariable String filename,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        Path videoPath = Paths.get(VIDEO_DIR).resolve(filename);
        if (!Files.exists(videoPath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        long fileSize = Files.size(videoPath);
        long rangeStart = 0;
        long rangeEnd = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                rangeStart = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    rangeEnd = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                rangeStart = 0;
                rangeEnd = fileSize - 1;
            }
        }

        if (rangeEnd > fileSize - 1) {
            rangeEnd = fileSize - 1;
        }

        long contentLength = rangeEnd - rangeStart + 1;

        InputStream inputStream = new BufferedInputStream(new FileInputStream(videoPath.toFile()));
        inputStream.skip(rangeStart);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(getContentType(filename));
        headers.setContentLength(contentLength);
        headers.add("Accept-Ranges", "bytes");
        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.PARTIAL_CONTENT);
    }

    private MediaType getContentType(String filename) {
        String lowerCaseName = filename.toLowerCase();
        if (lowerCaseName.endsWith(".mp4")) {
            return MediaType.valueOf("video/mp4");
        } else if (lowerCaseName.endsWith(".webm")) {
            return MediaType.valueOf("video/webm");
        } else if (lowerCaseName.endsWith(".ogg")) {
            return MediaType.valueOf("video/ogg");
        } else if (lowerCaseName.endsWith(".mkv")) {
            // mkv is not officially supported by MediaType, use octet-stream as fallback
            return MediaType.APPLICATION_OCTET_STREAM;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
