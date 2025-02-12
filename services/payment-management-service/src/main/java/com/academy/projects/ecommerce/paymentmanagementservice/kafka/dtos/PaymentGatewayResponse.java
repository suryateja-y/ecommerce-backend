package com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentGatewayResponse implements Serializable {
    private String paymentId;
    private PaymentStatus paymentStatus;
    private String paymentUrl;
}
