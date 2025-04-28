package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // add a new user with avatar upload
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> registerUser(
            @RequestPart(value = "userDTO", required = true) MultipartFile userDTOFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO;
        try {
            String userJson = new String(userDTOFile.getBytes());
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

    @GetMapping("/index")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
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
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId,
            @RequestPart(value = "userDTO", required = true) MultipartFile userDTOFile,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO;
        try {
            String userJson = new String(userDTOFile.getBytes());
            userDTO = objectMapper.readValue(userJson, UserDTO.class);
            userDTO.convertRoleIdToRoleIds();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        try {
            UserDTO updatedUser = userService.updateUser(userId, userDTO, imageFile);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
