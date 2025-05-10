package com.karhacter.movies_webapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

// import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    // @ExceptionHandler(ResourceNotFoundException.class)
    // public ResponseEntity<?>
    // handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest
    // request) {
    // Map<String, Object> body = new HashMap<>();
    // body.put("timestamp", LocalDateTime.now());
    // body.put("status", HttpStatus.NOT_FOUND.value());
    // body.put("error", "Resource Not Found");
    // body.put("message", ex.getMessage());
    // body.put("path", request.getDescription(false).replace("uri=", ""));
    // return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    // }

    // @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    // public ResponseEntity<?>
    // handleOptimisticLockingFailureException(ObjectOptimisticLockingFailureException
    // ex,
    // WebRequest request) {
    // Map<String, Object> body = new HashMap<>();
    // body.put("timestamp", LocalDateTime.now());
    // body.put("status", HttpStatus.CONFLICT.value());
    // body.put("error", "Conflict");
    // body.put("message",
    // "The resource was updated or deleted by another transaction. Please refresh
    // and try again.");
    // body.put("path", request.getDescription(false).replace("uri=", ""));
    // return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    // }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getClass().getSimpleName());
        body.put("message", ex.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
