package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String userId, String addressId) {
        super("User: '" + userId + "' does not have address '" + addressId + "'!!!");
    }
}
