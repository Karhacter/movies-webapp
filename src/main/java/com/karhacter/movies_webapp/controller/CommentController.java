package com.karhacter.movies_webapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.dto.CommentDTO;
import com.karhacter.movies_webapp.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        CommentDTO savedComment = commentService.createComment(commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByMovie(@PathVariable Long movieId) {
        List<CommentDTO> comments = commentService.getCommentsByMovieId(movieId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/movie/slug/{slug}")
    public ResponseEntity<List<CommentDTO>> getCommentsByMovie(@PathVariable String slug) {
        List<CommentDTO> comments = commentService.getCommentsByMovieSlug(slug);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommentDTO>> getCommentsByMovieTitle(@RequestParam String movieTitle) {
        List<CommentDTO> comments = commentService.getCommentsByMovieTitle(movieTitle);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CommentDTO>> getRecentComments(
            @RequestParam(defaultValue = "10") int limit) {
        List<CommentDTO> comments = commentService.getRecentComments(limit);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/report/{commentId}")
    public ResponseEntity<String> reportComment(
            @PathVariable Long commentId,
            @RequestParam String reason) {
        commentService.reportComment(commentId, reason);
        return new ResponseEntity<>("Comment reported successfully", HttpStatus.OK);
    }

    // restore comment
    @PostMapping("/restore/{id}")
    public ResponseEntity<String> restoreComment(@PathVariable Long id) {
        String message = commentService.restore(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // soft delete
    @PostMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        String message = commentService.softDelete(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/count/movie/{movieId}")
    public ResponseEntity<Long> countCommentsByMovieId(@PathVariable Long movieId) {
        long count = commentService.countCommentsByMovieId(movieId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
