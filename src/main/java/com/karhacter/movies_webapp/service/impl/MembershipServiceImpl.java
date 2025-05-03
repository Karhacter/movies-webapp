package com.karhacter.movies_webapp.service.impl;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.dto.MembershipPackageDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseResponseDTO;
import com.karhacter.movies_webapp.entity.MembershipPackage;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.repository.MembershipPackageRepository;
import com.karhacter.movies_webapp.repository.PaymentRepository;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.service.MembershipService;
import com.karhacter.movies_webapp.entity.Payment;
import com.karhacter.movies_webapp.entity.PaymentMethod;
import com.karhacter.movies_webapp.entity.PaymentStatus;

@Service
public class MembershipServiceImpl implements MembershipService {

        @Autowired
        private MembershipPackageRepository membershipPackageRepository;

        @Autowired
        private UserRepo userRepository;

        @Autowired
        private PaymentRepository paymentRepository;

        @Override
        public List<MembershipPackageDTO> getAllMembershipPackages() {
                List<MembershipPackage> packages = membershipPackageRepository.findAll();
                return packages.stream()
                                .map(pkg -> new MembershipPackageDTO(pkg.getId(), pkg.getName(), pkg.getPrice(),
                                                pkg.getDurationDays()))
                                .collect(Collectors.toList());
        }

        @Override
        public MembershipPackageDTO getMembershipPackageById(Long id) {
                return membershipPackageRepository.findById(id)
                                .map(pkg -> new MembershipPackageDTO(pkg.getId(), pkg.getName(), pkg.getPrice(),
                                                pkg.getDurationDays()))
                                .orElse(null);
        }

        @Override
        public MembershipPurchaseResponseDTO purchaseMembership(Long userId, Long packageId,
                        PaymentMethod paymentMethod, String idempotencyToken) {
                if (idempotencyToken != null && !idempotencyToken.isEmpty()) {
                        Payment existingPayment = paymentRepository.findByIdempotencyToken(idempotencyToken);
                        if (existingPayment != null) {
                                // Return existing response if payment with idempotencyToken exists
                                User user = existingPayment.getUser();
                                MembershipPackage membershipPackage = membershipPackageRepository.findById(packageId)
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Membership package not found"));
                                return new MembershipPurchaseResponseDTO(
                                                true,
                                                "Membership purchase already processed",
                                                membershipPackage.getName(),
                                                membershipPackage.getPrice(),
                                                user.getMembershipExpiration(),
                                                user.getUserID(),
                                                user.getUsername());
                        }
                }

                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

                if (user.isPremium()) {
                        throw new RuntimeException("User is already a premium member and cannot purchase again.");
                }

                MembershipPackage membershipPackage = membershipPackageRepository.findById(packageId)
                                .orElseThrow(() -> new RuntimeException("Membership package not found"));

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime currentExpiration = user.getMembershipExpiration();

                // Determine new expiration date
                LocalDateTime newExpirationDate;
                if (currentExpiration != null && currentExpiration.isAfter(now)) {
                        // Extend from current expiration
                        newExpirationDate = currentExpiration.plusDays(membershipPackage.getDurationDays());
                } else {
                        // New expiration from now
                        newExpirationDate = now.plusDays(membershipPackage.getDurationDays());
                }

                // Check for duplicate recent payment (same user, package, method, within short
                // time)
                boolean isDuplicate = paymentRepository.existsByUserAndDescriptionAndMethodAndPaymentDateAfter(
                                user,
                                "Membership purchase: " + membershipPackage.getName(),
                                paymentMethod,
                                now.minusMinutes(5) // 5 minutes window for duplicate
                );

                if (isDuplicate) {
                        // Optionally reject duplicate purchase
                        throw new RuntimeException(
                                        "Duplicate membership purchase detected. Please wait before retrying.");
                }

                // Create and save payment record
                Payment payment = new Payment();
                payment.setUser(user);
                payment.setAmount(membershipPackage.getPrice());
                payment.setMethod(paymentMethod);
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setDescription("Membership purchase: " + membershipPackage.getName());
                payment.setTransactionId(java.util.UUID.randomUUID().toString());
                payment.setIdempotencyToken(idempotencyToken);
                payment.setPaymentDate(java.util.Date.from(ZonedDateTime.now().toInstant()));
                paymentRepository.save(payment);

                // Update user membership status
                user.setPremium(true);
                user.setMembershipExpiration(newExpirationDate);
                userRepository.save(user);

                return new MembershipPurchaseResponseDTO(
                                true,
                                "Membership purchased successfully",
                                membershipPackage.getName(),
                                membershipPackage.getPrice(),
                                newExpirationDate,
                                user.getUserID(),
                                user.getUsername());
        }
}
