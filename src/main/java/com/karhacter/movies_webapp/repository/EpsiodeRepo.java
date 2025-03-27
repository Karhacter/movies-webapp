package com.karhacter.movies_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.karhacter.movies_webapp.entity.Episode;

@Repository
public interface EpsiodeRepo extends JpaRepository<Episode, Long> {
    Episode findByEpisodeNumber(Integer episodeNumber);
}
