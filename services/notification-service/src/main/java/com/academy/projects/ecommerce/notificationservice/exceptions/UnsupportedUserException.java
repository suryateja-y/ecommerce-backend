package com.academy.projects.ecommerce.notificationservice.exceptions;

import lombok.ToString;

@ToString
public class UnsupportedUserException extends RuntimeException {
    public UnsupportedUserException(String userType) {
        super("'" + userType + "' is not supported yet!!!");
    }
}
