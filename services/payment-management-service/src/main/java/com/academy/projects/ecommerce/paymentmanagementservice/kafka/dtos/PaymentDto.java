package com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto implements Serializable {
    private Payment payment;
    private Action action;
    private ActionStatus actionStatus;
    private String message;
}
