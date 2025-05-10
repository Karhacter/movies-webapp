package com.karhacter.movies_webapp.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Payment;
import com.karhacter.movies_webapp.entity.PaymentMethod;
import com.karhacter.movies_webapp.entity.PaymentStatus;
import com.karhacter.movies_webapp.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
        boolean existsByUserAndDescriptionAndMethodAndPaymentDateAfter(User user, String description,
                        PaymentMethod method,
                        LocalDateTime paymentDate);

        Payment findByIdempotencyToken(String idempotencyToken);

        // Search payments by transaction ID, user name, amount, date, status
        @Query("SELECT p FROM Payment p JOIN p.user u WHERE " +
                        "(:transactionId IS NULL OR LOWER(p.transactionId) LIKE LOWER(CONCAT('%', :transactionId, '%'))) AND "
                        +
                        "(:user IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :user, '%'))) AND " +
                        "(:amount IS NULL OR p.amount = :amount) AND " +
                        "(:date IS NULL OR FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m-%d') = :date) AND " +
                        "(:status IS NULL OR p.status = :status)")
        Page<Payment> searchPayments(@Param("transactionId") String transactionId,
                        @Param("user") String user,
                        @Param("amount") Double amount,
                        @Param("date") String date,
                        @Param("status") PaymentStatus status,
                        Pageable pageable);
}
