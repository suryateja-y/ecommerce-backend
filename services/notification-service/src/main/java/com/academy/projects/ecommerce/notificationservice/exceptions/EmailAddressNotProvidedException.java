package com.academy.projects.ecommerce.notificationservice.exceptions;

import lombok.ToString;

@ToString
public class EmailAddressNotProvidedException extends RuntimeException {
    public EmailAddressNotProvidedException(String registryKey) {
        super("Email address not provided for register '" + registryKey + "'!!!");
    }
}
