package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Comment;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.entity.Movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieId(Long movieId);

    List<Comment> findByUser(User user);

    List<Comment> findByMovie(Movie movie);

    long countByMovieId(Long movieId);

    List<Comment> findByMovieSlug(String slug);

    List<Comment> findByMovieSlug(String slug, Sort sort);

    Page<Comment> findByMovieSlug(String slug, Pageable pageable);
}
