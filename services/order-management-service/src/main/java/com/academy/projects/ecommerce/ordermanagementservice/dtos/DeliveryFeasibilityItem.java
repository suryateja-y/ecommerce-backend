package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryFeasibilityItem implements Serializable {
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
}
