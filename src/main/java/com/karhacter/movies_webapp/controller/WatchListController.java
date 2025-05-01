package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.WatchlistDTO;
import com.karhacter.movies_webapp.service.WatchListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/watchlists")
public class WatchListController {

    @Autowired
    private WatchListService watchlistService;

    @PostMapping("/add")
    public WatchlistDTO addMovieToWatchlist(
            @org.springframework.web.bind.annotation.RequestBody com.karhacter.movies_webapp.dto.AddWatchlistRequest request) {
        return watchlistService.addMovieToWatchlist(request.getUserId(), request.getMovieId());
    }

    @GetMapping("/index/{userId}")
    public ResponseEntity<List<WatchlistDTO>> getUserWatchlist(@PathVariable Long userId) {
        List<WatchlistDTO> watchlists = watchlistService.getUserWatchlist(userId);
        return new ResponseEntity<>(watchlists, HttpStatus.OK);
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
    public ResponseEntity<WatchlistDTO> toggleWatchlist(@RequestParam Long userId, @RequestParam Long movieId) {
        WatchlistDTO watchlistDTO = watchlistService.toggleWatchlist(userId, movieId);
        return new ResponseEntity<>(watchlistDTO, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkWatchlistStatus(
            @org.springframework.web.bind.annotation.RequestParam java.util.Map<String, String> params) {
        try {
            Long userId = Long.parseLong(params.get("userId"));
            Long movieId = Long.parseLong(params.get("movieId"));
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
