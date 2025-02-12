package com.academy.projects.ecommerce.productservice.exceptions;

import lombok.ToString;

@ToString
public class IdNotProvidedException extends RuntimeException {
    public IdNotProvidedException(String message) {
        super(message);
    }
}
