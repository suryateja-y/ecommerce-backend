package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class PreOrderNotFoundException extends RuntimeException {
    public PreOrderNotFoundException(String orderId) {
        super("Order with id '" + orderId + "' could not be found!!!");
    }
}
