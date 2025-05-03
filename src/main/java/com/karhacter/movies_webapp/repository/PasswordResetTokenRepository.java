package com.karhacter.movies_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.karhacter.movies_webapp.entity.PasswordResetToken;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
}
