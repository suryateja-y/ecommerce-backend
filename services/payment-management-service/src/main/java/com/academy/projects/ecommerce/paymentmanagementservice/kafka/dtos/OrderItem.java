package com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {
    private String productId;
    private String productName;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
