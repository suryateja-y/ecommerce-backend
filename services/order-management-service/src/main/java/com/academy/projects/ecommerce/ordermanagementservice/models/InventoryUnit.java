package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryUnit extends BaseModel {
    @NotBlank(message = "Variant Id should be mentioned")
    private String variantId;

    @NotBlank(message = "Seller Id should be mentioned")
    private String sellerId;

    @NotNull(message = "Quantity should be mentioned")
    private Long quantity;

    @NotNull(message = "Unit Price should be mentioned")
    @Positive(message = "Unit Price should be a positive value")
    private BigDecimal unitPrice;
}
