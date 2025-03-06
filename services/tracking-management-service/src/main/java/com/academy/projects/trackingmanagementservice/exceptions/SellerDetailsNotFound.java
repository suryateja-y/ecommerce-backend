package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class SellerDetailsNotFound extends RuntimeException {
    public SellerDetailsNotFound(String message) {
        super(message);
    }
}
