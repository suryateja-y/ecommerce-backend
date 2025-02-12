package com.academy.projects.ecommerce.approvalmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class ApprovalProcessAlreadyCompletedException extends RuntimeException {
    public ApprovalProcessAlreadyCompletedException(String requestId) {
        super("Approval Process has already been completed for this request: '" + requestId + "'!!!");
    }
}
