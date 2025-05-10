package com.karhacter.movies_webapp.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.dto.PaymentDTO;
import com.karhacter.movies_webapp.entity.Payment;
import com.karhacter.movies_webapp.repository.PaymentRepository;
import com.karhacter.movies_webapp.service.PaymentService;
import com.karhacter.movies_webapp.entity.PaymentStatus;

import java.util.Optional;

@Service

public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

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
            dto.setUserName(payment.getUser().getName());
            return dto;
        }
        return null;
    }

    @Override
    public Page<PaymentDTO> getPagePayments(Pageable pageable) {
        logger.info("Service: Getting payments for page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Payment> paymentPage = paymentRepository.findAll(pageable);

        return paymentPage.map(this::convertToPaymentDTO);
    }

    private PaymentDTO convertToPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();

        // Map simple properties
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setDescription(payment.getDescription());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setUserName(payment.getUser().getName());

        // Explicitly handle user ID mapping
        if (payment.getUser() != null) {
            dto.setUserId(payment.getUser().getUserID()); // Explicitly use the ID you want
        }

        return dto;
    }

    @Override
    public String DeletePayemnt(Long id) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) {
            return "Payment with id " + id + " not found.";
        }
        paymentRepository.deleteById(id);
        return "Payment with id " + id + " has been deleted successfully.";
    }

    @Override
    public PaymentDTO changeStatus(Long id, PaymentDTO paymentDTO) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) {
            throw new RuntimeException("Payment with id " + id + " not found.");
        }
        Payment payment = paymentOpt.get();
        payment.setStatus(paymentDTO.getStatus());
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToPaymentDTO(updatedPayment);
    }

    @Override
    public Page<PaymentDTO> searchPayments(String transactionId, String user, Double amount, String date,
            PaymentStatus status,
            Pageable pageable) {
        Page<Payment> paymentsPage = paymentRepository.searchPayments(transactionId, user, amount, date, status,
                pageable);
        return paymentsPage.map(this::convertToPaymentDTO);
    }

    @Override
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) {
            throw new RuntimeException("Payment with id " + id + " not found.");
        }
        Payment payment = paymentOpt.get();
        payment.setAmount(paymentDTO.getAmount());
        payment.setDescription(paymentDTO.getDescription());
        payment.setMethod(paymentDTO.getMethod());
        payment.setStatus(paymentDTO.getStatus());
        payment.setTransactionId(paymentDTO.getTransactionId());
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToPaymentDTO(updatedPayment);
    }
}
