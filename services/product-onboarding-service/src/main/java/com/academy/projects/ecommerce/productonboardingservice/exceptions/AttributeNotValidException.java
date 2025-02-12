package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class AttributeNotValidException extends RuntimeException {
    public AttributeNotValidException(String message) {
        super(message);
    }
}
