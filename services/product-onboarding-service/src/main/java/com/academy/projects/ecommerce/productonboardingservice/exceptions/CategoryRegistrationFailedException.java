package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class CategoryRegistrationFailedException extends RuntimeException {
    public CategoryRegistrationFailedException(String categoryId, String errorMessage) {
        super("Registering category '" + categoryId + "' failed: " + errorMessage);
    }
}
