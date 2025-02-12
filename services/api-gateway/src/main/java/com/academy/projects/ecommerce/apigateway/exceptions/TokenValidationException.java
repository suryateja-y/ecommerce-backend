package com.academy.projects.ecommerce.apigateway.exceptions;

import lombok.ToString;

@ToString
public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String message) {
        super(message);
    }
}
