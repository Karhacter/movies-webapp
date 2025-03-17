package com.karhacter.movies_webapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.entity.Watchlist;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.payloads.WatchlistDTO;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.repository.WatchListRepo;
import com.karhacter.movies_webapp.service.WatchListService;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchListRepo watchlistRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MovieRepo movieRepo;

    @Override
    public List<WatchlistDTO> getUserWatchlist(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return watchlistRepo.findByUser(user);
    }

    @Override
    public WatchlistDTO addMovieToWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (!watchlistRepo.existsByUserAndMovie(user, movie)) {
            Watchlist watchlist = new Watchlist();
            watchlist.setUser(user);
            watchlist.setMovie(movie);
            watchlist = watchlistRepo.save(watchlist);

            return new WatchlistDTO(watchlist.getId(), watchlist.getCreated_at(), user.getUserID(), movie.getId());
        }
        throw new RuntimeException("Movie already in watchlist");
    }

    @Override
    public String DeleteList(Long id) {
        Watchlist existingCategory = watchlistRepo.findById(id)
                .orElseThrow(() -> new APIException("Category with id '" + id + "' not found !!!"));

        // delete category
        watchlistRepo.delete(existingCategory);

        return "Category with id '" + id + "' deleted successfully !!!";
    }
}
