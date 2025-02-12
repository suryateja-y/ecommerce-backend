package com.academy.projects.ecommerce.notificationservice.exceptions;

import lombok.ToString;

@ToString
public class EmailDetailsNotAvailableException extends RuntimeException {
    public EmailDetailsNotAvailableException(String registerKey) {
        super("Email details not available for '" + registerKey + "'!!!");
    }
}
