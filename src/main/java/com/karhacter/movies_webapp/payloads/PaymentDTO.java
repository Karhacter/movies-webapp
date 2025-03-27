package com.karhacter.movies_webapp.payloads;

import java.util.Date;
import com.karhacter.movies_webapp.entity.PaymentStatus;
import com.karhacter.movies_webapp.entity.PaymentMethod;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private long id;
    private long userId;
    private String userName;
    private double amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String transactionId;
    private Date paymentDate;
    private Date dueDate;
    private String description;
}