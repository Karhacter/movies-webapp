package com.karhacter.movies_webapp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Role;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.UserDTO;
import com.karhacter.movies_webapp.repository.RoleRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.service.UserService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        try {
            Optional<User> existingUser = userRepo.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent()) {
                throw new APIException("⚠️ Email đã tồn tại: " + userDTO.getEmail());
            }

            User user = modelMapper.map(userDTO, User.class);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            // Handle roles
            Set<Role> roles = new HashSet<>();
            if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
                // Use requested roles
                for (Long roleId : userDTO.getRoleIds()) {
                    Role role = roleRepo.findById(roleId)
                            .orElseThrow(() -> new APIException("Role not found with id: " + roleId));
                    roles.add(role);
                }
            } else {
                // Add default USER role (role_id = 3)
                Role userRole = roleRepo.findById(3L) // Thay đổi ở đây
                        .orElseThrow(() -> new APIException("Default role not found"));
                roles.add(userRole);
            }
            user.setRoles(roles);

            // Save user first
            User registeredUser = userRepo.save(user);

            // Then save user_roles relationship
            registeredUser.setRoles(roles);
            userRepo.save(registeredUser);

            // Return full user details
            return getUserById(registeredUser.getUserID());

        } catch (DataIntegrityViolationException e) {
            throw new APIException("User already exists with emailId: " + userDTO.getEmail());
        } catch (Exception e) {
            throw new APIException("Error occurred while registering user: " + e.getMessage());
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        // Sử dụng lại logic từ getUserById
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRoleIds(user.getRoles().stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet()));

        return userDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOs = userRepo.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return userDTOs;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        userDTO.setRoleIds(user.getRoles().stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet())); // Lấy ID của Role

        return userDTO;

    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public String deleteUser(Long userId) {
        // delete simple user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        userRepo.delete(user);
        return "User with userId " + userId + " deleted successfully!!!";
    }

}
