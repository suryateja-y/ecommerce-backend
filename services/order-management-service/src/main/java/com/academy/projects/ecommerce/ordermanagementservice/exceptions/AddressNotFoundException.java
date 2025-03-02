package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String addressId, String message) {
        super("Address '" + addressId + "' does not exist!!! >>> " + message);
    }
}
