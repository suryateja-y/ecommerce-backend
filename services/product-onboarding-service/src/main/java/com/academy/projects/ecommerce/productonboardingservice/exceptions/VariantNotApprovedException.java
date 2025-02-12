package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class VariantNotApprovedException extends RuntimeException {
    public VariantNotApprovedException(String variantId) {
        super("Variant " + variantId + " is not approved!!!");
    }
}
