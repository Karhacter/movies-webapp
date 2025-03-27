package com.karhacter.movies_webapp.service;

import java.util.List;
import com.karhacter.movies_webapp.payloads.CommentDTO;

public interface CommentService {
    // Create a new comment on a movie
    CommentDTO createComment(CommentDTO commentDTO);

    // Get all comments for a specific movie
    List<CommentDTO> getCommentsByMovieId(Long movieId);

    // Get all comments by a specific user
    List<CommentDTO> getCommentsByUserId(Long userId);

    // Update an existing comment
    CommentDTO updateComment(Long commentId, CommentDTO commentDTO);

    // Delete a comment
    void deleteComment(Long commentId);

    // Get recent comments (for homepage or activity feed)
    List<CommentDTO> getRecentComments(int limit);

    // Report a comment as inappropriate
    void reportComment(Long commentId, String reason);
}
