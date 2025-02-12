package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class NoUnPaidPaymentFoundException extends RuntimeException {
    public NoUnPaidPaymentFoundException(String orderId) {
        super("No unpaid payment found for order id '" + orderId + "'!!!");
    }
}
