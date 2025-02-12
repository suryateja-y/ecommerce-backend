package com.academy.projects.ecommerce.productservice.exceptions;

import lombok.ToString;

@ToString
public class ProductNotProvidedException extends RuntimeException {
    public ProductNotProvidedException(String message) {
        super(message);
    }
}
