package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class EmployeeApprovalRegistrationException extends RuntimeException {
    public EmployeeApprovalRegistrationException(String id) {
        super("Failed to register the employee: '" + id + "' for the approval!!!");
    }
}
