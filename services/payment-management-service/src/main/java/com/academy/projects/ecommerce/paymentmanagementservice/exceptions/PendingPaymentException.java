package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class PendingPaymentException extends RuntimeException {
    public PendingPaymentException(String customerId) {
        super("Pending Payment Exists for the Customer: " + customerId);
    }
}
