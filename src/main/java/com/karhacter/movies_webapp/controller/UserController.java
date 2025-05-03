package com.karhacter.movies_webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karhacter.movies_webapp.dto.UserDTO;
import com.karhacter.movies_webapp.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    // add a new user with avatar upload
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> registerUser(
            @RequestPart(value = "userDTO", required = true) MultipartFile userDTOFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO;
        try {
            String userJson = new String(userDTOFile.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            userDTO = objectMapper.readValue(userJson, UserDTO.class);
            userDTO.convertRoleIdToRoleIds();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        try {
            UserDTO newUser = userService.registerUser(userDTO, imageFile);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Link Google account
    @PostMapping("/{userId}/link/google")
    public ResponseEntity<String> linkGoogleAccount(@PathVariable Long userId, @RequestParam String googleId) {
        try {
            userService.linkGoogleAccount(userId, googleId);
            return ResponseEntity.ok("Google account linked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Link Facebook account
    @PostMapping("/{userId}/link/facebook")
    public ResponseEntity<String> linkFacebookAccount(@PathVariable Long userId, @RequestParam String facebookId) {
        try {
            userService.linkFacebookAccount(userId, facebookId);
            return ResponseEntity.ok("Facebook account linked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Unlink Google account
    @PostMapping("/{userId}/unlink/google")
    public ResponseEntity<String> unlinkGoogleAccount(@PathVariable Long userId) {
        try {
            userService.unlinkGoogleAccount(userId);
            return ResponseEntity.ok("Google account unlinked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Unlink Facebook account
    @PostMapping("/{userId}/unlink/facebook")
    public ResponseEntity<String> unlinkFacebookAccount(@PathVariable Long userId) {
        try {
            userService.unlinkFacebookAccount(userId);
            return ResponseEntity.ok("Facebook account unlinked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/index")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserDTO>> getPageUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        logger.info("Received request for movies - Page: {}, Size: {}, SortBy: {}, SortOrder: {}",
                page, size, sortBy, sortOrder);

        // Ensure page is not negative
        page = Math.max(0, page);

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> users = userService.getPageUser(pageable);

        logger.info("Returning movies - Current Page: {}, Total Pages: {}, Total Elements: {}",
                users.getNumber(), users.getTotalPages(), users.getTotalElements());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/detail/id/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/detail/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    // delete user
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    // soft delete user
    @PostMapping("/soft-delete/{userId}")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.softDelete(userId), HttpStatus.OK);
    }

    // restore user
    @PostMapping("/restore/{userId}")
    public ResponseEntity<String> restoreUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.restore(userId), HttpStatus.OK);
    }

    // update user with avatar upload
    @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
            @RequestPart(value = "userDTO", required = true) MultipartFile userDTOFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO;
        try {
            String userJson = new String(userDTOFile.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            userDTO = objectMapper.readValue(userJson, UserDTO.class);
            userDTO.convertRoleIdToRoleIds();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        try {
            UserDTO updatedUser = userService.updateUser(userId, userDTO, imageFile);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (com.karhacter.movies_webapp.exception.APIException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
