package com.academy.projects.ecommerce.productsearchservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUnitContainer implements Serializable {
    private String id;
    private String productId;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
}
