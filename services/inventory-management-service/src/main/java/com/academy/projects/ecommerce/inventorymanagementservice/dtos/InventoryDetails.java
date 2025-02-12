package com.academy.projects.ecommerce.inventorymanagementservice.dtos;

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
public class InventoryDetails implements Serializable {
    private String variantId;
    private String sellerId;
    private BigDecimal unitPrice;
    private boolean inStock;
}
