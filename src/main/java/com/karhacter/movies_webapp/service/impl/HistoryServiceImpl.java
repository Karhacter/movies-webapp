package com.karhacter.movies_webapp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.repository.HistoryRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.service.HistoryService;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private MovieRepo movieRepository;

    @Override
    public List<History> getUserHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return historyRepo.findByUser(user);
    }

    @Override
    public History saveWatchedMovie(Long userId, Long movieId, int watchProgress) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));

        History history;
        if (historyRepo.existsByUserAndMovie(user, movie)) {
            history = historyRepo.findByUser(user).stream()
                    .filter(h -> h.getMovie().equals(movie))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("History not found"));
            history.setWatchProgress(watchProgress);
            history.setLastWatched(new Date());
        } else {
            history = new History();
            history.setUser(user);
            history.setMovie(movie);
            history.setWatchProgress(watchProgress);
            history.setLastWatched(new Date());
        }
        return historyRepo.save(history);
    }
}
