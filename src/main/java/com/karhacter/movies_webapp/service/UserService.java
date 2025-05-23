package com.karhacter.movies_webapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.UserDTO;
import com.karhacter.movies_webapp.payloads.LoginResponse;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO, MultipartFile imageFile);

    UserDTO getUserByEmail(String email);

    Page<UserDTO> getPageUser(Pageable pageable);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO, MultipartFile imageFile);

    String deleteUser(Long userId);

    String login(LoginResponse dto);

    String softDelete(Long userId);

    String restore(Long userId);

    // New methods for linking/unlinking social accounts
    void linkGoogleAccount(Long userId, String googleId) throws Exception;

    void linkFacebookAccount(Long userId, String facebookId) throws Exception;

    void unlinkGoogleAccount(Long userId) throws Exception;

    void unlinkFacebookAccount(Long userId) throws Exception;

    void requestPasswordReset(String email) throws Exception;

    void resetPassword(String token, String newPassword) throws Exception;

    void changePassword(Long userId, String oldPassword, String newPassword) throws Exception;

    // New search method
    Page<UserDTO> searchUsers(String name, String email, String role, Pageable pageable);
}
