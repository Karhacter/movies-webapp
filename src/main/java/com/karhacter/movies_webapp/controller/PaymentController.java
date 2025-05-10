package com.karhacter.movies_webapp.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.karhacter.movies_webapp.entity.PaymentStatus;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.dto.MembershipPackageDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseRequestDTO;
import com.karhacter.movies_webapp.dto.MembershipPurchaseResponseDTO;
import com.karhacter.movies_webapp.dto.PaymentDTO;
import com.karhacter.movies_webapp.service.MembershipService;
import com.karhacter.movies_webapp.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepo userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

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

    @GetMapping("/page")
    public ResponseEntity<Page<PaymentDTO>> getPagePayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy, // Changed default from "rating" to "id"
            @RequestParam(defaultValue = "desc") String sortOrder) {

        logger.info("Received request for payments - Page: {}, Size: {}, SortBy: {}, SortOrder: {}",
                page, size, sortBy, sortOrder);

        // Validate page number
        page = Math.max(0, page);

        // Validate sortBy field - only allow sorting by existing Payment fields
        List<String> validSortFields = Arrays.asList("id", "amount", "paymentDate", "paymentMethod");
        if (!validSortFields.contains(sortBy.toLowerCase())) {
            sortBy = "id"; // Fallback to default sort field
            logger.warn("Invalid sort field requested. Falling back to default sort by id");
        }

        // Create pageable with sorting
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentDTO> payments = paymentService.getPagePayments(pageable);

        logger.info("Returning payments - Current Page: {}, Total Pages: {}, Total Elements: {}",
                payments.getNumber(), payments.getTotalPages(), payments.getTotalElements());

        return ResponseEntity.ok(payments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        String result = paymentService.DeletePayemnt(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PaymentDTO> detailPayment(@PathVariable Long id) {
        PaymentDTO result = paymentService.getPaymentById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentDTO> changePaymentStatus(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.changeStatus(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PaymentDTO>> searchPayments(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        List<String> validSortFields = List.of("id", "amount", "paymentDate", "paymentMethod");
        if (!validSortFields.contains(sortBy.toLowerCase())) {
            sortBy = "id";
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PaymentDTO> paymentsPage = paymentService.searchPayments(transactionId, user, amount, date, status,
                pageable);

        return ResponseEntity.ok(paymentsPage);
    }
}
