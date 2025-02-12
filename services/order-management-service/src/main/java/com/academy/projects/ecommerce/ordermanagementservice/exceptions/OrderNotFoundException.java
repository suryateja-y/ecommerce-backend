package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order with id '" + orderId + "' not found!!!");
    }

    public OrderNotFoundException(String orderId, String customerId) {
        super("Order with id '" + orderId + "' could not be found under customer id '" + customerId + "'!!!");
    }
}
