package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.dto.MembershipPackageDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseRequestDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseResponseDTO;
import com.karhacter.movies_webapp.service.MembershipService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private com.karhacter.movies_webapp.repository.UserRepo userRepository;

    @GetMapping("/membership-packages")
    public ResponseEntity<List<MembershipPackageDTO>> getMembershipPackages() {
        List<MembershipPackageDTO> packages = membershipService.getAllMembershipPackages();
        return ResponseEntity.ok(packages);
    }

    @PostMapping("/purchase-membership")
    public ResponseEntity<MembershipPurchaseResponseDTO> purchaseMembership(
            @RequestBody MembershipPurchaseRequestDTO request) {
        MembershipPurchaseResponseDTO response = membershipService.purchaseMembership(
                request.getUserId(),
                request.getPackageId(),
                request.getPaymentMethod(),
                request.getIdempotencyToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-premium-status")
    public ResponseEntity<Boolean> getUserPremiumStatus(@RequestParam Long userId) {
        com.karhacter.movies_webapp.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.isPremium());
    }
}
