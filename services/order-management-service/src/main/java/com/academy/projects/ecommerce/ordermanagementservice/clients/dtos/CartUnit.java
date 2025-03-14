package com.academy.projects.ecommerce.ordermanagementservice.clients.dtos;

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
public class CartUnit implements Serializable {
    private String productId;
    private String productName;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
    private boolean inStock;
}
