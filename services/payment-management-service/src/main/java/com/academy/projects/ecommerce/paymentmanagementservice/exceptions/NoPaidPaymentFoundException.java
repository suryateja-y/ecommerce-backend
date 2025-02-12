package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class NoPaidPaymentFoundException extends RuntimeException {
    public NoPaidPaymentFoundException(String orderId) {
        super("No paid payment found for order id '" + orderId + "'!!!");
    }
}
