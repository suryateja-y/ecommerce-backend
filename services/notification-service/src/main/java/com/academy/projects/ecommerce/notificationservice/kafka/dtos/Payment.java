package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private String id;
    private Date createdAt;
    private Date modifiedAt;
    private String orderId;
    private String paymentId;
    private String customerId;
    private BigDecimal totalAmount;
    private String paymentUrl;
    private CurrencyType currency = CurrencyType.INR;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String reason;
}
