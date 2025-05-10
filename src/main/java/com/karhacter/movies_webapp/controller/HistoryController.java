package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.HistoryDTO;
import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.payloads.SaveWatchedMovieRequest;
import com.karhacter.movies_webapp.service.HistoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<?> getUserHistory(@PathVariable String userId) {
        if (userId == null || userId.equals("null")) {
            return ResponseEntity.badRequest().body("Invalid userId parameter");
        }
        Long userIdLong;
        try {
            userIdLong = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("userId must be a valid number");
        }
        List<History> historyList = historyService.getUserHistory(userIdLong);
        return ResponseEntity.ok(historyList);
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<?> getUserHistoryDTO(@PathVariable String userId, Pageable pageable) {
        if (userId == null || userId.equals("null")) {
            return ResponseEntity.badRequest().body("Invalid userId parameter");
        }
        Long userIdLong;
        try {
            userIdLong = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("userId must be a valid number");
        }
        Page<HistoryDTO> historyPage = historyService.getUserHistoryDTO(userIdLong, pageable);
        return ResponseEntity.ok(historyPage);
    }

    @PostMapping("/save")
    public ResponseEntity<History> saveWatchedMovie(@RequestBody SaveWatchedMovieRequest request) {
        History favoriteHistory = historyService.saveWatchedMovie(request.getUserId(), request.getMovieId(),
                request.getWatchProgress());
        return new ResponseEntity<>(favoriteHistory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHistory(@PathVariable Long id) {
        String result = historyService.deleteHistoryById(id);
        return ResponseEntity.ok(result);
    }
}
