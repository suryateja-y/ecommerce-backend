package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Seller with email " + email + " not found!!!");
    }
}
