package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String company;
    private String brandName;
    private Category category;
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;
    private String approvalId;
}
