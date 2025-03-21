package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

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
    private int quantity;
    private BigDecimal unitPrice;
    private String unitPriceString;
    private BigDecimal totalPrice;
    private String totalPriceString;
}
