package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.History;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.payloads.HistoryDTO;

@Repository
public interface HistoryRepo extends JpaRepository<History, Integer> {
    List<History> findByUser(User user);

    List<HistoryDTO> findByUserDTO(User user);

    boolean existsByUserAndMovie(User user, Movie movie);
}
