package com.academy.projects.ecommerce.paymentmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class UnhandledEventTypeException extends RuntimeException {
    public UnhandledEventTypeException(String eventType) {
        super("Event type '" + eventType + "' is not handled yet!!!");
    }
}
