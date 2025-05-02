package com.karhacter.movies_webapp.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Payment;
import com.karhacter.movies_webapp.entity.PaymentMethod;
import com.karhacter.movies_webapp.entity.User;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByUserAndDescriptionAndMethodAndPaymentDateAfter(User user, String description, PaymentMethod method,
            LocalDateTime paymentDate);

    Payment findByIdempotencyToken(String idempotencyToken);
}
