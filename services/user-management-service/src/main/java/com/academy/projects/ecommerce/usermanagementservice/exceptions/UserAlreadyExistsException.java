package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import lombok.ToString;

@ToString
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email, UserType userType) {
        super("User with email '" + email + "' already exists as '" + userType + "'!!!");
    }
}
