package com.academy.projects.ecommerce.inventorymanagementservice.exceptions;

import lombok.ToString;

@ToString
public class AddressNotProvidedException extends RuntimeException {
    public AddressNotProvidedException(String message) {
        super(message);
    }
}
