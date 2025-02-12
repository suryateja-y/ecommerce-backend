package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class CategoryNotProvidedException extends RuntimeException {
    public CategoryNotProvidedException() {
        super("Category ID not provided");
    }
}
