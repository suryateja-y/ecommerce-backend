package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}
