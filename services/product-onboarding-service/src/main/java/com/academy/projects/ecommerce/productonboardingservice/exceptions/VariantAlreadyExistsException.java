package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class VariantAlreadyExistsException extends RuntimeException {
    public VariantAlreadyExistsException(String id) {
        super("Variant with id '" + id + "' already exists!!!");
    }
}
