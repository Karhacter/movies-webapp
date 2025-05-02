package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.dto.MembershipPackageDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseResponseDTO;
import com.karhacter.movies_webapp.entity.PaymentMethod;

public interface MembershipService {
    List<MembershipPackageDTO> getAllMembershipPackages();

    MembershipPackageDTO getMembershipPackageById(Long id);


    MembershipPurchaseResponseDTO purchaseMembership(Long userId, Long packageId, PaymentMethod paymentMethod, String idempotencyToken);
}
