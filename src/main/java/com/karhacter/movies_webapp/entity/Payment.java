package com.karhacter.movies_webapp.entity;

import java.util.Date;
import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin("0.0")
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate = new Date();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

enum PaymentMethod {
    CARD, PAYPAL, CRYPTO
}

enum PaymentStatus {
    PENDING, COMPLETED, FAILED
}