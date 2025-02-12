package com.academy.projects.ecommerce.paymentmanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseModel {
    private String orderId;
    private String paymentId;
    private String customerId;
    private BigDecimal totalAmount;
    private String paymentUrl;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency = CurrencyType.INR;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String reason;
}
