package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.karhacter.movies_webapp.entity.Episode;
import com.karhacter.movies_webapp.entity.Movie;

@Repository
public interface EpsiodeRepo extends JpaRepository<Episode, Long> {
    Episode findByEpisodeNumber(Integer episodeNumber);

    List<Episode> findByMovie(Movie movie);
}
