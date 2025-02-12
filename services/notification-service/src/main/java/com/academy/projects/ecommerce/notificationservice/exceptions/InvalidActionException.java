package com.academy.projects.ecommerce.notificationservice.exceptions;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import lombok.ToString;

@ToString
public class InvalidActionException extends RuntimeException {
    public InvalidActionException(Action action) {
        super("Action: '" + action.toString() + "' is not implemented yet!!!");
    }
}
