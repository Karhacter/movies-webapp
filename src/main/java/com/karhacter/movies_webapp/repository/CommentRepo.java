package com.karhacter.movies_webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Comment;
import com.karhacter.movies_webapp.entity.User;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieId(Long movieId);

    List<Comment> findByUser(User user);
}
