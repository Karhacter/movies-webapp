package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.payloads.UserDTO;
import com.karhacter.movies_webapp.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userSerivce;

    // add a new user
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userSerivce.registerUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/index")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userSerivce.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/detail/{userId}")
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        return new ResponseEntity<>(userSerivce.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/detail/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(String email) {
        return new ResponseEntity<>(userSerivce.getUserByEmail(email), HttpStatus.OK);
    }

    // delete user
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(Long userId) {
        return new ResponseEntity<>(userSerivce.deleteUser(userId), HttpStatus.OK);
    }
}
