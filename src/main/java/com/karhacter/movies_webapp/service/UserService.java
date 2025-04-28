package com.karhacter.movies_webapp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.UserDTO;
import com.karhacter.movies_webapp.payloads.LoginResponse;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO, MultipartFile imageFile);

    UserDTO getUserByEmail(String email); // Đã thêm phương thức này

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO, MultipartFile imageFile);

    String deleteUser(Long userId);

    String login(LoginResponse dto);

    String softDelete(Long userId);

    String restore(Long userId);
}
