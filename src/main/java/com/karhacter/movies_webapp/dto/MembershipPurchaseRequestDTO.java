package com.karhacter.movies_webapp.dto;

import com.karhacter.movies_webapp.entity.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPurchaseRequestDTO {
    private Long userId;
    private Long packageId;
    private PaymentMethod paymentMethod;
    private String idempotencyToken;
}
