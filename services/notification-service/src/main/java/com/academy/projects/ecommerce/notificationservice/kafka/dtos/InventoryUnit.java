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
public class InventoryUnit implements Serializable {
    private String id;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;
    private String approvalId;
}
