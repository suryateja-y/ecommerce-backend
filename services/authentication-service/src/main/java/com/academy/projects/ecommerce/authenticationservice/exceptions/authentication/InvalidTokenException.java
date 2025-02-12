package com.academy.projects.ecommerce.authenticationservice.exceptions.authentication;

import lombok.ToString;

@ToString
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
