package com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequestDto implements Serializable {
    private String approvalId;
    private ApprovalStatus approvalStatus;
    private String requesterId;
}
