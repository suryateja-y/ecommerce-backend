package com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Action implements Serializable {
    private String actedBy;
    private String comment;
    private ApprovalStatus approvalStatus;
    private Date actedOn;
}
