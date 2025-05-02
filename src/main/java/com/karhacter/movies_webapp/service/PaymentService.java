package com.karhacter.movies_webapp.service;

import com.karhacter.movies_webapp.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Long id);
}
