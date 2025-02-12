package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class CancellationFailedException extends RuntimeException {
    public CancellationFailedException(String paymentId, String reason) {
        super("Failed to cancel payment '" + paymentId + "': '" + reason + "'!!!");
    }
}
