package com.academy.projects.ecommerce.usermanagementservice.exceptions;

import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import lombok.ToString;

@ToString
public class InvalidateStateRequest extends RuntimeException {
    public InvalidateStateRequest(UserState userState) {
        super("Invalid state for the direct update: '" + userState.toString() + "'!!!");
    }
}
