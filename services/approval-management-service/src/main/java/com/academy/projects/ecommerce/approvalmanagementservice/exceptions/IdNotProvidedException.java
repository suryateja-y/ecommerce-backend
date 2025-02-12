package com.academy.projects.ecommerce.approvalmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class IdNotProvidedException extends RuntimeException {
    public IdNotProvidedException() {
        super("Id for the request is not provided!!!");
    }
}
