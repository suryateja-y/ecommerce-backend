package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class InvalidStatusRequest extends RuntimeException {
    public InvalidStatusRequest(String message) {
        super(message);
    }
}
