package com.karhacter.movies_webapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.dto.PaymentDTO;
import com.karhacter.movies_webapp.entity.Payment;
import com.karhacter.movies_webapp.repository.PaymentRepository;
import com.karhacter.movies_webapp.service.PaymentService;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setDescription(paymentDTO.getDescription());
        payment.setMethod(paymentDTO.getMethod());
        payment.setStatus(paymentDTO.getStatus());
        payment.setTransactionId(paymentDTO.getTransactionId());
        // User should be set by service caller, skipping here for demo

        Payment savedPayment = paymentRepository.save(payment);
        paymentDTO.setId(savedPayment.getId());
        paymentDTO.setPaymentDate(savedPayment.getPaymentDate());
        return paymentDTO;
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            PaymentDTO dto = new PaymentDTO();
            dto.setId(payment.getId());
            dto.setAmount(payment.getAmount());
            dto.setDescription(payment.getDescription());
            dto.setMethod(payment.getMethod());
            dto.setStatus(payment.getStatus());
            dto.setTransactionId(payment.getTransactionId());
            dto.setPaymentDate(payment.getPaymentDate());
            dto.setUserId(payment.getUser().getUserID());
            dto.setUserName(payment.getUser().getUsername());
            return dto;
        }
        return null;
    }
}
