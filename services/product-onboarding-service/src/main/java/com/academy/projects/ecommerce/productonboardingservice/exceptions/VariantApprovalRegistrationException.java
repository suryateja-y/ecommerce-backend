package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class VariantApprovalRegistrationException extends RuntimeException {
    public VariantApprovalRegistrationException(String variantId) {
        super("Failed to register the variant: '" + variantId + "' for the approval!!!");
    }
}
