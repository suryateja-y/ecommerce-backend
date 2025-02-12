package com.academy.projects.ecommerce.productsearchservice.exceptions;

import lombok.ToString;

@ToString
public class AddressNotProvidedException extends RuntimeException {
    public AddressNotProvidedException(String message) {
        super(message);
    }
}
