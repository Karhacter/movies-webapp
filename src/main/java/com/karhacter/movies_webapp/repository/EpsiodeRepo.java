package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.entity.Movie;

@Repository
public interface EpsiodeRepo extends JpaRepository<Episode, Long> {
    List<Episode> findByEpisodeNumber(Integer episodeNumber);

    List<Episode> findByEpisodeNumberAndMovie(Integer episodeNumber, Movie movie);

    List<Episode> findByMovie(Movie movie);

    @Query("SELECT e FROM Episode e WHERE e.id IN (SELECT MIN(e2.id) FROM Episode e2 GROUP BY e2.movie.id)")
    Page<Episode> findDistinctByMovie(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT e.season) FROM Episode e WHERE e.movie.id = :movieId")
    long countDistinctSeasonsByMovieId(Long movieId);

    @Query("SELECT COUNT(e) FROM Episode e WHERE e.movie.id = :movieId")
    long countByMovieId(Long movieId);
}
