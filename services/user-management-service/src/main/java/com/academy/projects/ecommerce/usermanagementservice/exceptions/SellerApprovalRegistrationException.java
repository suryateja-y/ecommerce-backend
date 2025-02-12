package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class SellerApprovalRegistrationException extends RuntimeException {
    public SellerApprovalRegistrationException(String id) {
        super("Failed to register the seller: '" + id + "' for the approval!!!");
    }
}
