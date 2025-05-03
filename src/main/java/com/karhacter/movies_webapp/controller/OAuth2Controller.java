package com.karhacter.movies_webapp.controller;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.security.JwtUtil;

@RestController
@CrossOrigin
public class OAuth2Controller {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/oauth2UserInfo")
    public Map<String, Object> oauth2UserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return Collections.singletonMap("error", "User not authenticated");
        }
        return oauth2User.getAttributes();
    }

    @GetMapping("/api/oauth2/loginSuccess")
    public Map<String, Object> oauth2LoginSuccess(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.singletonMap("error", "User not authenticated");
        }

        String username = authentication.getName();
        User user = userRepo.findByEmail(username).orElse(null);

        // If user not found by email, try to find by googleId or facebookId
        if (user == null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

            // Try googleId
            if (attributes.containsKey("sub")) {
                String googleId = (String) attributes.get("sub");
                user = userRepo.findByGoogleId(googleId).orElse(null);
            }

            // Try facebookId if still null
            if (user == null && attributes.containsKey("id")) {
                String facebookId = (String) attributes.get("id");
                user = userRepo.findByFacebookId(facebookId).orElse(null);
            }
        }

        if (user == null) {
            return Collections.singletonMap("error", "User not found");
        }

        String token = jwtUtil.generateToken(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("roles", user.getRoles());

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            response.put("oauth2Attributes", oauth2User.getAttributes());
        }

        return response;
    }

    @GetMapping(value = "/api/oauth/login", produces = "application/json")
    @ResponseBody
    public Map<String, String> login() {
        Map<String, String> oauth2Urls = new HashMap<>();
        oauth2Urls.put("google", "/oauth2/authorization/google");
        oauth2Urls.put("facebook", "/oauth2/authorization/facebook");
        return oauth2Urls;
    }

    @GetMapping(value = "/api/test", produces = "application/json")
    @ResponseBody
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint working");
        return response;
    }
}
