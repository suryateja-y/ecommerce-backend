package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String id) {
        super("Product with id '" + id + "' already exists!!!");
    }
}
