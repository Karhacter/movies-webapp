package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.karhacter.movies_webapp.entity.Comment;
import com.karhacter.movies_webapp.entity.Movie;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.CommentDTO;
import com.karhacter.movies_webapp.repository.CommentRepo;
import com.karhacter.movies_webapp.repository.MovieRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
        // Get user and movie
        User user = userRepo.findById(commentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", commentDTO.getUserId()));
        Movie movie = movieRepo.findById(commentDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", commentDTO.getMovieId()));

        // Create and save comment
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setUser(user);
        comment.setMovie(movie);
        comment = commentRepo.save(comment);

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public List<CommentDTO> getCommentsByMovieId(Long movieId) {
        List<Comment> comments = commentRepo.findByMovieId(movieId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<Comment> comments = commentRepo.findByUser(user);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        // Get existing comment
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        // Verify user owns this comment
        if (comment.getUser().getUserID() != commentDTO.getUserId()) {
            throw new APIException("You can only update your own comments!");
        }

        // Update comment
        comment.setContent(commentDTO.getContent());
        comment = commentRepo.save(comment);

        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        commentRepo.delete(comment);
    }

    @Override
    public List<CommentDTO> getRecentComments(int limit) {
        return commentRepo.findAll(PageRequest.of(0, limit, Sort.by("createdAt").descending()))
                .getContent()
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void reportComment(Long commentId, String reason) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        // Here you could implement additional logic for handling reported comments
        // For example, setting a flag, sending notifications to moderators, etc.
        // For now, we'll just log the report
        System.out.println("Comment " + commentId + " reported for reason: " + reason);
    }
}