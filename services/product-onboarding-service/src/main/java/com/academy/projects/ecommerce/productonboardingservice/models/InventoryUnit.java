package com.academy.projects.ecommerce.productonboardingservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUnit extends BaseModel {
    @NotBlank(message = "Variant Id should be given")
    private String variantId;

    @NotBlank(message = "Seller Id should be given")
    private String sellerId;

    @NotEmpty(message = "Quantity should be given")
    @Positive(message = "Quantity should be a positive number")
    @CustomUpdate
    private Long quantity;

    @NotEmpty(message = "Price should be given")
    @Positive(message = "Price should be a positive decimal")
    @CustomUpdate
    private BigDecimal unitPrice;

    @CustomUpdate
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;

    @CustomUpdate
    private String approvalId;
}
