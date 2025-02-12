package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variant {
    private String id;
    private Product product;
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;
    private String approvalId;
}
