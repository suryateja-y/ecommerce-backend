package com.academy.projects.ecommerce.productservice.security;

import lombok.ToString;

@ToString
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
