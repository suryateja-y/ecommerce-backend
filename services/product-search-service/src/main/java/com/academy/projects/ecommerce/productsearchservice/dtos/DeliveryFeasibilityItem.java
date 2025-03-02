package com.academy.projects.ecommerce.productsearchservice.dtos;

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
public class DeliveryFeasibilityItem implements Serializable {
    private String variantId;
    private String sellerId;
    private int quantity;
    private BigDecimal unitPrice;
}
