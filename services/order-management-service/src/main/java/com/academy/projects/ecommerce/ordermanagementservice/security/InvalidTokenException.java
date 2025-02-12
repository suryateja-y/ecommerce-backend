package com.academy.projects.ecommerce.ordermanagementservice.security;

import lombok.ToString;

@ToString
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
