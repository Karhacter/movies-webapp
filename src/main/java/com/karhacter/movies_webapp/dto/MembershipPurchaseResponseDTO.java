package com.karhacter.movies_webapp.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPurchaseResponseDTO {
    private boolean success;
    private String message;
    private String membershipName;
    private double price;
    private LocalDateTime expirationDate;
    private Long userId;
    private String userName;
}
