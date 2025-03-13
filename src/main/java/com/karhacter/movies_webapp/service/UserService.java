package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.payloads.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    UserDTO getUserByEmail(String email); // Đã thêm phương thức này

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);
}
