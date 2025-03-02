package com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreOrderItem implements Serializable {
    private String id;
    private String productId;
    private String productName;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
