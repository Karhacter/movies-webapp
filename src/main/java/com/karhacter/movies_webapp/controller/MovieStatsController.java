package com.karhacter.movies_webapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.dto.MovieDTO;
import com.karhacter.movies_webapp.dto.MovieStatsDTO;
import com.karhacter.movies_webapp.service.MovieStatisService;

@RestController
@RequestMapping("/api/movies/stats")
public class MovieStatsController {

    @Autowired
    private MovieStatisService movieStatisService;

    public MovieStatsController(MovieStatisService movieStatisService) {
        this.movieStatisService = movieStatisService;
    }

    @GetMapping("/overall")
    public ResponseEntity<MovieStatsDTO> getOverallStats() {
        MovieStatsDTO stats = movieStatisService.getMovieStatistics();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/period/{period}")
    public ResponseEntity<MovieStatsDTO> getStatsByTimePeriod(@PathVariable String period) {
        MovieStatsDTO stats = movieStatisService.getMovieStatisticsByTimePeriod(period);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies(
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieDTO> movies = movieStatisService.getTopRatedMovies(limit);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<List<MovieDTO>> getMostViewedMovies(
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieDTO> movies = movieStatisService.getMostViewedMovies(limit);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/most-commented")
    public ResponseEntity<List<MovieDTO>> getMostCommentedMovies(
            @RequestParam(defaultValue = "10") int limit) {
        List<MovieDTO> movies = movieStatisService.getMostCommentedMovies(limit);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/rating-range")
    public ResponseEntity<List<MovieDTO>> getMoviesByRatingRange(
            @RequestParam double minRating,
            @RequestParam double maxRating) {
        List<MovieDTO> movies = movieStatisService.getMoviesByRatingRange(minRating, maxRating);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/duration-range")
    public ResponseEntity<List<MovieDTO>> getMoviesByDurationRange(
            @RequestParam int minDuration,
            @RequestParam int maxDuration) {
        List<MovieDTO> movies = movieStatisService.getMoviesByDurationRange(minDuration, maxDuration);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }
}