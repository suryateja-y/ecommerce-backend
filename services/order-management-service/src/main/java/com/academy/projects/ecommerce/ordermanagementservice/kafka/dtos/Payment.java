package com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.CurrencyType;
import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentMethod;
import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentStatus;
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
    private String orderId;
    private String paymentId;
    private String customerId;
    private BigDecimal totalAmount;
    private String paymentUrl;
    private Date createdAt;
    private CurrencyType currency;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
}
