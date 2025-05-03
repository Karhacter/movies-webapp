package com.karhacter.movies_webapp.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.karhacter.movies_webapp.dto.UserDTO;
import com.karhacter.movies_webapp.entity.Role;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.LoginResponse;
import com.karhacter.movies_webapp.repository.PasswordResetTokenRepository;
import com.karhacter.movies_webapp.repository.RoleRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.security.JwtUtil;
import com.karhacter.movies_webapp.service.UserService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;

    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;

    private final com.karhacter.movies_webapp.service.EmailService emailService;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, RoleRepo roleRepo,
            PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
            com.karhacter.movies_webapp.service.EmailService emailService,
            PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public void linkGoogleAccount(Long userId, String googleId) throws Exception {
        // Check if googleId is already linked to another user
        userRepo.findByGoogleId(googleId).ifPresent(user -> {
            if (user.getUserID() != userId) {
                throw new RuntimeException("Google account is already linked to another user.");
            }
        });

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setGoogleId(googleId);
        userRepo.save(user);
    }

    @Override
    public void linkFacebookAccount(Long userId, String facebookId) throws Exception {
        // Check if facebookId is already linked to another user
        userRepo.findByFacebookId(facebookId).ifPresent(user -> {
            if (user.getUserID() != userId) {
                throw new RuntimeException("Facebook account is already linked to another user.");
            }
        });

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFacebookId(facebookId);
        userRepo.save(user);
    }

    @Override
    public void unlinkGoogleAccount(Long userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setGoogleId(null);
        userRepo.save(user);
    }

    @Override
    public void unlinkFacebookAccount(Long userId) throws Exception {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFacebookId(null);
        userRepo.save(user);
    }

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public UserDTO registerUser(UserDTO userDTO, MultipartFile imageFile) {
        try {
            Optional<User> existingUser = userRepo.findByEmail(userDTO.getEmail());
            if (existingUser.isPresent()) {
                throw new APIException("⚠️ Email đã tồn tại: " + userDTO.getEmail());
            }

            User user = modelMapper.map(userDTO, User.class);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setStatusDelete(1);

            // Handle avatar image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String avatarUrl = uploadImage(imageFile);
                user.setAvatar(avatarUrl);
            }

            // Handle roles
            Set<Role> roles = new HashSet<>();
            if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
                for (Long roleId : userDTO.getRoleIds()) {
                    Role role = roleRepo.findById(roleId)
                            .orElseThrow(() -> new APIException("Role not found with id: " + roleId));
                    roles.add(role);
                }
            } else {
                Role userRole = roleRepo.findById(3L)
                        .orElseThrow(() -> new APIException("Default role not found"));
                roles.add(userRole);
            }
            user.setRoles(roles);

            User registeredUser = userRepo.save(user);
            registeredUser.setRoles(roles);
            userRepo.save(registeredUser);

            return getUserById(registeredUser.getUserID());

        } catch (DataIntegrityViolationException e) {
            throw new APIException("User already exists with emailId: " + userDTO.getEmail());
        } catch (Exception e) {
            throw new APIException("Error occurred while registering user: " + e.getMessage());
        }
    }

    public String uploadImage(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (Exception ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
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
                .collect(Collectors.toSet()));
        return userDTO;
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO, MultipartFile imageFile) {
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        // Check if email exists for a different user
        Optional<User> userWithEmail = userRepo.findByEmail(userDTO.getEmail());
        if (userWithEmail.isPresent() && userWithEmail.get().getUserID() != userId) {
            throw new APIException("Email already exists: " + userDTO.getEmail());
        }

        existingUser.setName(userDTO.getName());
        existingUser.setMobileNumber(userDTO.getMobileNumber());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setBalance(userDTO.getBalance());

        // Handle avatar update if imageFile provided
        if (imageFile != null && !imageFile.isEmpty()) {
            String avatarUrl = uploadImage(imageFile);
            existingUser.setAvatar(avatarUrl);
        } else if (userDTO.getAvatar() != null && !userDTO.getAvatar().isEmpty()) {
            existingUser.setAvatar(userDTO.getAvatar());
        }

        // Handle roles update
        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            for (Long roleId : userDTO.getRoleIds()) {
                Role role = roleRepo.findById(roleId)
                        .orElseThrow(() -> new APIException("Role not found with id: " + roleId));
                roles.add(role);
            }
            existingUser.setRoles(roles);
        }

        User updatedUser = userRepo.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public String softDelete(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setStatusDelete(0);
        userRepo.save(user);
        return "User soft-deleted successfully";
    }

    @Override
    public String restore(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setStatusDelete(1);
        userRepo.save(user);
        return "User restored successfully";
    }

    @Override
    public String deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        userRepo.delete(user);
        return "User with userId " + userId + " deleted successfully!!!";
    }

    @Override
    public String login(LoginResponse dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return jwtUtil.generateToken(user);
    }

    @Override
    public Page<UserDTO> getPageUser(Pageable pageable) {
        logger.info("Service: Getting movies for page: {}, size: {}", pageable.getPageNumber(),
                pageable.getPageSize());
        // No pageable findAllActive method, so fallback to filtering after fetching all
        // active movies
        List<User> activeUsers = userRepo.findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), activeUsers.size());
        List<User> pageContent = activeUsers.subList(start, end);
        Page<UserDTO> userDTOs = new PageImpl<>(
                pageContent.stream()
                        .map(movie -> modelMapper.map(movie, UserDTO.class))
                        .collect(Collectors.toList()),
                pageable,
                activeUsers.size());
        logger.info("Service: Returning {} movies for page: {}", userDTOs.getContent().size(),
                userDTOs.getNumber());
        return userDTOs;
    }

    @Override
    public void requestPasswordReset(String email) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'requestPasswordReset'");
    }

    @Override
    public void resetPassword(String token, String newPassword) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

}
