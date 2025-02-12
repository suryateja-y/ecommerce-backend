package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class IdNotProvidedException extends RuntimeException {
    public IdNotProvidedException() {
        super("Id not provided!!!");
    }
}
