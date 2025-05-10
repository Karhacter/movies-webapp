package com.karhacter.movies_webapp.controller;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.UserDTO;
import com.karhacter.movies_webapp.service.UserService;
import com.karhacter.movies_webapp.entity.Role;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.payloads.LoginResponse;
import com.karhacter.movies_webapp.repository.RoleRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {

        Optional<User> existingUser = userRepo.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new APIException("⚠️ Email already exists: " + userDTO.getEmail());
        }

        // Optional null-check to help catch issues earlier
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new APIException("⚠️ Password is required: " + userDTO.getEmail());
        }

        // add the body to save in db
        User newUser = modelMapper.map(userDTO, User.class);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Set roles, use default if not provided
        Set<Role> roles = userDTO.getRoleIds().stream()
                .map(id -> roleRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + id)))
                .collect(Collectors.toSet());
        if (roles.isEmpty()) {
            roles.add(roleRepo.findById(3L).orElseThrow(() -> new RuntimeException("Default role not found")));
        }
        newUser.setRoles(roles);

        // Save user and map it back to DTO for the response
        userRepo.save(newUser);
        UserDTO createdUserDTO = modelMapper.map(newUser, UserDTO.class);
        createdUserDTO.setRoleIds(newUser.getRoles().stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet()));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginResponse loginRequest, HttpServletResponse response) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            String email = loginRequest.getEmail();
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new APIException("User not found: " + email));
            String jwt = jwtUtil.generateToken(user);

            ResponseCookie cookie = ResponseCookie.from("token", jwt)
                    .httpOnly(false) // For development only
                    .secure(false) // For development only
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Lax") // More flexible for development
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "email", user.getEmail(),
                    "token", jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Starting logout process...");

        // Invalidate session if exists
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            System.out.println("Session invalidated");
        }

        String token = null;

        // Debug all headers
        System.out.println("Request headers:");
        java.util.Collections.list(request.getHeaderNames())
                .forEach(header -> System.out.println(header + ": " + request.getHeader(header)));

        // First try to get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            System.out.println("Found token in Authorization header");
        }

        // If no header token, check cookies
        if (token == null) {
            System.out.println("Checking cookies...");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    System.out.println("Cookie: " + cookie.getName() + "=" + cookie.getValue());
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        System.out.println("Found token in cookie");
                        break;
                    }
                }
            }
        }

        // Invalidate token if found
        boolean tokenInvalidated = false;
        if (token != null && !token.isEmpty()) {
            System.out.println("Invalidating token: " + token);
            jwtUtil.invalidateToken(token);
            tokenInvalidated = true;
        } else {
            System.out.println("No token found to invalidate");
        }

        // Clear client-side cookie
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("Logout complete. Token invalidated: " + tokenInvalidated);

        return ResponseEntity.ok(Map.of(
                "message", "Logged out",
                "tokenInvalidated", tokenInvalidated,
                "debug", "Check server logs for details"));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        System.out.println("Checking authentication...");

        // Check Authorization header first
        String token = jwtUtil.getTokenFromRequest(request);
        System.out.println("Token from header: " + token);

        // If no header token, check cookies
        if (token == null) {
            System.out.println("No token in header, checking cookies...");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                System.out.println("Found " + cookies.length + " cookies");
                for (Cookie cookie : cookies) {
                    System.out.println("Cookie: " + cookie.getName() + "=" + cookie.getValue());
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        System.out.println("Found token in cookie: " + token);
                        break;
                    }
                }
            }
        }

        if (token != null) {
            System.out.println("Validating token...");
            boolean isValid = jwtUtil.validateToken(token);
            System.out.println("Token valid: " + isValid);
            if (isValid) {
                String email = jwtUtil.getEmailFromToken(token);
                System.out.println("Token valid for email: " + email);
                com.karhacter.movies_webapp.entity.User user = userRepo.findByEmail(email).orElse(null);
                if (user != null) {
                    java.util.Set<String> roles = user.getRoles().stream()
                            .map(com.karhacter.movies_webapp.entity.Role::getRoleName)
                            .collect(java.util.stream.Collectors.toSet());
                    return ResponseEntity.ok(Map.of(
                            "loggedIn", true,
                            "email", email,
                            "roles", roles));
                } else {
                    return ResponseEntity.ok(Map.of(
                            "loggedIn", true,
                            "email", email,
                            "roles", java.util.Collections.emptySet()));
                }
            }
        }
        System.out.println("No valid token found");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("loggedIn", false));
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);

        if (token == null) {
            // Check cookies if token not found in header
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or missing token"));
        }

        String email = jwtUtil.getEmailFromToken(token);
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRoleIds(user.getRoles().stream().map(role -> role.getRoleId()).collect(Collectors.toSet()));

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/oauth2/token")
    public ResponseEntity<?> oauth2TokenExchange(@RequestBody Map<String, String> body) {
        String provider = body.get("provider");
        String accessToken = body.get("accessToken");

        if (provider == null || accessToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing provider or accessToken"));
        }

        Map<String, Object> userInfo;
        try {
            if ("google".equalsIgnoreCase(provider)) {
                userInfo = verifyGoogleAccessToken(accessToken);
            } else if ("facebook".equalsIgnoreCase(provider)) {
                userInfo = verifyFacebookAccessToken(accessToken);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Unsupported provider"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid access token"));
        }

        String email = (String) userInfo.get("email");
        if (email == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Email not found in user info"));
        }

        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName((String) userInfo.getOrDefault("name", email));
            newUser.setPassword(""); // No password for OAuth2 users
            // Assign default role
            newUser.setRoles(
                    Set.of(roleRepo.findById(3L).orElseThrow(() -> new RuntimeException("Default role not found"))));
            return userRepo.save(newUser);
        });

        String jwt = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "username", user.getUsername(),
                "email", user.getEmail()));
    }

    private Map<String, Object> verifyGoogleAccessToken(String idToken) throws Exception {
        // Decode the JWT ID token locally to extract claims
        String[] parts = idToken.split("\\.");
        if (parts.length < 2) {
            throw new Exception("Invalid ID token format");
        }
        String payload = parts[1];
        byte[] decodedBytes = java.util.Base64.getUrlDecoder().decode(payload);
        String jsonPayload = new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);

        System.out.println("Decoded Google ID token payload: " + jsonPayload);

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Map<String, Object> claims = mapper.readValue(jsonPayload, Map.class);

        // Basic validation: check issuer and audience if needed
        String issuer = (String) claims.get("iss");
        if (!"https://accounts.google.com".equals(issuer) && !"accounts.google.com".equals(issuer)) {
            throw new Exception("Invalid issuer: " + issuer);
        }

        // You can add more validations here (audience, expiry, etc.)

        return claims;
    }

    private Map<String, Object> verifyFacebookAccessToken(String accessToken) throws Exception {
        String appAccessToken = "9660972777356540";
        String url = "https://graph.facebook.com/debug_token?input_token=" + accessToken + "&access_token="
                + appAccessToken;
        java.net.URL facebookUrl = new java.net.URL(url);
        try (java.io.InputStream is = facebookUrl.openStream()) {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> debugResponse = mapper.readValue(is, Map.class);
            Map<String, Object> data = (Map<String, Object>) debugResponse.get("data");
            if (data == null || !(Boolean) data.getOrDefault("is_valid", false)) {
                throw new Exception("Invalid Facebook access token");
            }
        }

        // Get user info
        String userInfoUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        java.net.URL userInfoUrlObj = new java.net.URL(userInfoUrl);
        try (java.io.InputStream is = userInfoUrlObj.openStream()) {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(is, Map.class);
        }
    }
}
