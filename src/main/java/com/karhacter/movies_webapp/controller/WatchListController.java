package com.karhacter.movies_webapp.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.WatchlistDTO;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.payloads.AddWatchlistRequest;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.service.WatchListService;

@RestController
@RequestMapping("/api/watchlists")
public class WatchListController {

    @Autowired
    private WatchListService watchlistService;

    @Autowired
    private MovieRepo movieRepo;

    @PostMapping("/add")
    public WatchlistDTO addMovieToWatchlist(@RequestBody AddWatchlistRequest request) {
        return watchlistService.addMovieToWatchlist(request.getUserId(), request.getMovieId());
    }

    @GetMapping("/index/{userId}")
    public ResponseEntity<Page<WatchlistDTO>> getUserWatchlist(@PathVariable Long userId, Pageable pageable) {
        Page<WatchlistDTO> watchlistsPage = watchlistService.getUserWatchlist(userId, pageable);
        return new ResponseEntity<>(watchlistsPage, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteList(@PathVariable Long id) {
        return new ResponseEntity<>(watchlistService.DeleteList(id), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeMovieFromWatchlist(@RequestParam Long userId, @RequestParam Long movieId) {
        String result = watchlistService.removeMovieFromWatchlist(userId, movieId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/toggle")
    public ResponseEntity<WatchlistDTO> toggleWatchlist(@RequestParam Long userId, @RequestParam String movieSlug) {
        Optional<Movie> movieOpt = movieRepo.findBySlug(movieSlug);
        if (movieOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Movie movie = movieOpt.get();
        WatchlistDTO watchlistDTO = watchlistService.toggleWatchlist(userId, movie.getId());
        return new ResponseEntity<>(watchlistDTO, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkWatchlistStatus(@RequestParam Map<String, String> params) {
        try {
            Long userId = Long.parseLong(params.get("userId"));
            String movieIdStr = params.get("movieId");
            String movieSlug = params.get("movieSlug");
            Long movieId = null;
            if (movieIdStr != null) {
                movieId = Long.parseLong(movieIdStr);
            } else if (movieSlug != null) {
                Optional<Movie> movieOpt = movieRepo.findBySlug(movieSlug);
                if (movieOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid movieSlug");
                }
                movieId = movieOpt.get().getId();
            } else {
                return ResponseEntity.badRequest().body("Missing movieId or movieSlug");
            }
            boolean inWatchlist = watchlistService.isMovieInWatchlist(userId, movieId);
            return ResponseEntity.ok(new java.util.HashMap<String, Boolean>() {
                {
                    put("inWatchlist", inWatchlist);
                }
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid or missing parameters");
        }
    }
}
