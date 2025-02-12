package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(String variantId) {
        super("Variant with id '" + variantId + "' not found!!!");
    }
}
