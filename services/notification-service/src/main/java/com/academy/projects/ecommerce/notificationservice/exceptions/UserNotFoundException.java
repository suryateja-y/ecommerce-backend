package com.academy.projects.ecommerce.notificationservice.exceptions;

import lombok.ToString;

@ToString
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User '" + email + "' not found!!!");
    }
}
