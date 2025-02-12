package com.academy.projects.ecommerce.approvalmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class ApprovalRequestNotFoundException extends RuntimeException {
    public ApprovalRequestNotFoundException(String id) {
        super("Approval Request with id '" + id + "' not found!!!");
    }
}
