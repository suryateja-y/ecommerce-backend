package com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentMethod;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentIntentDto implements Serializable {
    private PaymentStatus paymentStatus;
    private String paymentId;
    private PaymentMethod paymentMethod;
    private String reason;
}
