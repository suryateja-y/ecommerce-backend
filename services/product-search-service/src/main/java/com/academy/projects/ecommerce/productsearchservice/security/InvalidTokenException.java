package com.academy.projects.ecommerce.productsearchservice.security;

import lombok.ToString;

@ToString
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
