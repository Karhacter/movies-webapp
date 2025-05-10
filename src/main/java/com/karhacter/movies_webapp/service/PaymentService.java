package com.karhacter.movies_webapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.karhacter.movies_webapp.dto.PaymentDTO;
import com.karhacter.movies_webapp.entity.PaymentStatus;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);

    PaymentDTO getPaymentById(Long id);

    Page<PaymentDTO> getPagePayments(Pageable pageable);

    // delete payment
    String DeletePayemnt(Long id);

    // change status between PENDING, COMPLETED, FAILED, REFUNDED
    PaymentDTO changeStatus(Long id, PaymentDTO paymentDTO);

    // New search method
    Page<PaymentDTO> searchPayments(String transactionId, String user, Double amount, String date, PaymentStatus status, Pageable pageable);

    // New update payment method
    PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO);
}
