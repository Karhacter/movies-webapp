package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.payloads.HistoryDTO;
import com.karhacter.movies_webapp.service.HistoryService;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @GetMapping("/favorites/{userId}")
    public List<HistoryDTO> getUserHistory(@PathVariable Long userId) {
        return historyService.getUserHistory(userId);
    }

    @PostMapping("/save")
    public ResponseEntity<History> saveWatchedMovie(@RequestParam Long userId, @RequestParam Long movieId,
            @RequestParam int watchProgress) {
        History favoriteHistory = historyService.saveWatchedMovie(userId, movieId, watchProgress);
        return new ResponseEntity<>(favoriteHistory, HttpStatus.OK);
    }
}
