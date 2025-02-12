package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class NoActivePaymentException extends RuntimeException {
    public NoActivePaymentException(String customerId, String orderId) {
        super("No active payment found for order '" + orderId + "' for customer '" + customerId + "'!!!");
    }
}
