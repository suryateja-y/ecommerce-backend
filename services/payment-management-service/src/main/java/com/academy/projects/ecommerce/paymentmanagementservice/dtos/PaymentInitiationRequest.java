package com.academy.projects.ecommerce.paymentmanagementservice.dtos;

import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.OrderItem;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitiationRequest implements Serializable {
    private String orderId;
    private String customerId;
    private Set<OrderItem> orderItems;
    private BigDecimal totalAmount;
}
