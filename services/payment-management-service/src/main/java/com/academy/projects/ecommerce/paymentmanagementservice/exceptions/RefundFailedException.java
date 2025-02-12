package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class RefundFailedException extends RuntimeException {
    public RefundFailedException(String paymentId, String reason) {
        super("Refund failed for payment id '" + paymentId + "': '" + reason + "'!!!");
    }
}
