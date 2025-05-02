package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.dto.MembershipPackageDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseResponseDTO;
import com.karhacter.movies_webapp.service.MembershipService;
import com.karhacter.movies_webapp.entity.PaymentMethod;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping("/membership-packages")
    public ResponseEntity<List<MembershipPackageDTO>> getMembershipPackages() {
        List<MembershipPackageDTO> packages = membershipService.getAllMembershipPackages();
        return ResponseEntity.ok(packages);
    }

    @PostMapping("/purchase-membership")
    public ResponseEntity<MembershipPurchaseResponseDTO> purchaseMembership(@RequestParam Long userId,
            @RequestParam Long packageId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam(required = false) String idempotencyToken) {
        MembershipPurchaseResponseDTO response = membershipService.purchaseMembership(userId, packageId, paymentMethod, idempotencyToken);
        return ResponseEntity.ok(response);
    }
}
