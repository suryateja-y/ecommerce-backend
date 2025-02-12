package com.academy.projects.ecommerce.productonboardingservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class InventoryRequestDto implements Serializable {
    @NotBlank(message = "Variant Id should be given")
    private String variantId;

    @NotBlank(message = "Quantity should be given")
    @Positive(message = "Quantity should be a positive number")
    private Long quantity;

    @NotBlank(message = "Unit Price should be given")
    @Positive(message = "Unit Price should be a positive value")
    private BigDecimal unitPrice;
}
