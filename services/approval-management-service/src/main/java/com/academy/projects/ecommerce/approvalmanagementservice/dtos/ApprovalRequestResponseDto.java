package com.academy.projects.ecommerce.approvalmanagementservice.dtos;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequestResponseDto implements Serializable {
    private ApprovalRequest approvalRequest;
    private String comment;
}
