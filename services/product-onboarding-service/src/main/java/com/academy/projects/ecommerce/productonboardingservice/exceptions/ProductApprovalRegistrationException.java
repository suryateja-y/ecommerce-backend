package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class ProductApprovalRegistrationException extends RuntimeException {
    public ProductApprovalRegistrationException(String productId) {
        super("Failed to register the product: '" + productId + "' for the approval!!!");
    }
}
