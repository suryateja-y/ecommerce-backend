package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class TrackingNumberGenerationException extends RuntimeException {
    public TrackingNumberGenerationException(String message) {
        super(message);
    }
}
