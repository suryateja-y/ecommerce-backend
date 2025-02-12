package com.academy.projects.ecommerce.authenticationservice.exceptions.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.UserType;
import lombok.ToString;

@ToString
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email, UserType userType) {
        super("User " + email + " already exists as '" + userType + "'!!!");
    }
}
