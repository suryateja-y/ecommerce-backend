package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class PendingOrderExistsException extends RuntimeException {
    public PendingOrderExistsException(String customerId) {
        super("Pending order exists for customer '" + customerId + "'!!!");
    }
}
