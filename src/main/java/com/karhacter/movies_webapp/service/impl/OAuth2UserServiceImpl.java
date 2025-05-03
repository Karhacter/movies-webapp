package com.karhacter.movies_webapp.service.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.repository.UserRepo;

import lombok.RequiredArgsConstructor;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepo userRepo;
    private final com.karhacter.movies_webapp.service.EmailService emailService;

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google or facebook
        Map<String, Object> attributes = oauth2User.getAttributes();

        String email = null;
        String googleId = null;
        String facebookId = null;
        String name = null;
        String avatar = null;

        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            googleId = (String) attributes.get("sub");
            name = (String) attributes.get("name");
            avatar = (String) attributes.get("picture");
        } else if ("facebook".equals(registrationId)) {
            email = (String) attributes.get("email");
            facebookId = (String) attributes.get("id");
            name = (String) attributes.get("name");
            avatar = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
        }

        Optional<User> userOptional = userRepo.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();

            // Check if Google ID is already linked to another user
            if ("google".equals(registrationId) && googleId != null && !googleId.isEmpty()) {
                Optional<User> existingGoogleUser = userRepo.findByGoogleId(googleId);
                if (existingGoogleUser.isPresent() && !existingGoogleUser.get().getEmail().equals(email)) {
                    throw new OAuth2AuthenticationException("Google account is already linked to another user");
                }
                if (user.getGoogleId() == null || user.getGoogleId().isEmpty()) {
                    user.setGoogleId(googleId);
                }
            }

            // Check if Facebook ID is already linked to another user
            if ("facebook".equals(registrationId) && facebookId != null && !facebookId.isEmpty()) {
                Optional<User> existingFacebookUser = userRepo.findByFacebookId(facebookId);
                if (existingFacebookUser.isPresent() && !existingFacebookUser.get().getEmail().equals(email)) {
                    throw new OAuth2AuthenticationException("Facebook account is already linked to another user");
                }
                if (user.getFacebookId() == null || user.getFacebookId().isEmpty()) {
                    user.setFacebookId(facebookId);
                }
            }

            user.setName(name);
            user.setAvatar(avatar);
        } else {
            // Create new user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAvatar(avatar);
            user.setProvider(registrationId);
            user.setGoogleId(googleId);
            user.setFacebookId(facebookId);
            user.setStatusDelete(1);
            user.setPremium(false);
            user.setBalance(0);
            userRepo.save(user);

            // Send welcome email after first login
            String subject = "Welcome to Movies Webapp!";
            String text = "Hi " + name + ",\n\nWelcome to Movies Webapp! We're glad to have you on board.";
            emailService.sendSimpleMessage(email, subject, text);
        }

        // Return a DefaultOAuth2User with ROLE_USER authority
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email");
    }
}
