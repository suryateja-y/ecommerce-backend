package com.academy.projects.ecommerce.authenticationservice.exceptions.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.UserType;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String email, UserType userType) {
        super("User with email: " + email + " not found as '" + userType + "'!!!");
    }
    public UserNotFoundException(final String email) {
        super("User with email: " + email + " not found!!!");
    }
}
