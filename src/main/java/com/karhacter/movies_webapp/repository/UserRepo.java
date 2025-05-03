package com.karhacter.movies_webapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByFacebookId(String facebookId);
}
