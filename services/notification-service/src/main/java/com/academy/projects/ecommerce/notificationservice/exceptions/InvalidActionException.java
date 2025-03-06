package com.academy.projects.ecommerce.notificationservice.exceptions;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.TrackingStatus;
import lombok.ToString;

@ToString
public class InvalidActionException extends RuntimeException {
    public InvalidActionException(Action action) {
        super("Action: '" + action.toString() + "' is not implemented yet!!!");
    }
    public InvalidActionException(TrackingStatus trackingStatus) {
        super("Tracking Status: '" + trackingStatus.toString() + "' is not implemented yet!!!");
    }
}
