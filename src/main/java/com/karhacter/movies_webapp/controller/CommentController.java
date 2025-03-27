package com.karhacter.movies_webapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.payloads.CommentDTO;
import com.karhacter.movies_webapp.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUser(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
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
}