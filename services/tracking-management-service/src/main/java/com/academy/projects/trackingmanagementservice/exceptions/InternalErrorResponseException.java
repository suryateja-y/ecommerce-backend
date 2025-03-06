package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class InternalErrorResponseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
    public InternalErrorResponseException(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
