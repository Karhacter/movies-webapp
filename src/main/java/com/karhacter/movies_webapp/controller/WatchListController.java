package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.payloads.WatchlistDTO;
import com.karhacter.movies_webapp.service.WatchListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/watchlists")
public class WatchListController {

    @Autowired
    private WatchListService watchlistService;

    @PostMapping("/add")
    public WatchlistDTO addMovieToWatchlist(@RequestParam Long userId, @RequestParam Long movieId) {
        return watchlistService.addMovieToWatchlist(userId, movieId);
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
}
