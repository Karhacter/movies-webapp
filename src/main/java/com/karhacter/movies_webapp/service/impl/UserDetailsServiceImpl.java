package com.karhacter.movies_webapp.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Role;
import com.karhacter.movies_webapp.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

        private final UserRepo userRepo;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                com.karhacter.movies_webapp.entity.User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
                return org.springframework.security.core.userdetails.User.builder()
                                .username(user.getEmail()) // not username
                                .password(user.getPassword())
                                .roles(user.getRoles().stream().map(Role::getRoleName).toArray(String[]::new))
                                .build();
        }
}