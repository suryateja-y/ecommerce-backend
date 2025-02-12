package com.academy.projects.ecommerce.approvalmanagementservice.exceptions;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import lombok.ToString;

@ToString
public class InvalidApprovalStatusException extends RuntimeException {
    public InvalidApprovalStatusException(ApprovalStatus approvalStatus) {
        super("Approval status: '" + approvalStatus + "' is not valid!!!");
    }
}
