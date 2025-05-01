package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.karhacter.movies_webapp.dto.WatchlistDTO;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.entity.Watchlist;
import com.karhacter.movies_webapp.exception.APIException;
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
    private ModelMapper modelMapper;

    @Autowired
    private MovieRepo movieRepo;

    @Override
    public List<WatchlistDTO> getUserWatchlist(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return watchlistRepo.findByUser(user);
    }

    @Override
    @Transactional
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
    @Transactional
    public String removeMovieFromWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        java.util.Optional<Watchlist> watchlistOpt = watchlistRepo.findByUserAndMovie(user, movie);
        if (watchlistOpt.isPresent()) {
            watchlistRepo.delete(watchlistOpt.get());
            return "Movie removed from watchlist successfully";
        } else {
            return "Movie not found in watchlist";
        }
    }

    @Override
    @Transactional
    public WatchlistDTO toggleWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        java.util.Optional<Watchlist> watchlistOpt = watchlistRepo.findByUserAndMovie(user, movie);
        if (watchlistOpt.isPresent()) {
            watchlistRepo.delete(watchlistOpt.get());
            return null;
        } else {
            Watchlist watchlist = new Watchlist();
            watchlist.setUser(user);
            watchlist.setMovie(movie);
            watchlist = watchlistRepo.save(watchlist);
            return new WatchlistDTO(watchlist.getId(), watchlist.getCreated_at(), user.getUserID(), movie.getId());
        }
    }

    @Override
    @Transactional
    public String DeleteList(Long id) {
        Watchlist existingCategory = watchlistRepo.findById(id)
                .orElseThrow(() -> new APIException("Category with id '" + id + "' not found !!!"));

        // delete category
        watchlistRepo.delete(existingCategory);

        return "Category with id '" + id + "' deleted successfully !!!";
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMovieInWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return watchlistRepo.existsByUserAndMovie(user, movie);
    }

}
