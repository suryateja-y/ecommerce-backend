package com.academy.projects.ecommerce.approvalmanagementservice.dtos;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateStatusRequestDto implements Serializable {
    private String comment;
    private ApprovalStatus approvalStatus;
}
