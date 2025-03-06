package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class CustomerOrAddressNotFoundException extends RuntimeException {
    public CustomerOrAddressNotFoundException(String customerId, String addressId) {
        super("Customer: '" + customerId + "' not found!!! Or Address: '" + addressId + "' not found!!!");
    }
}
