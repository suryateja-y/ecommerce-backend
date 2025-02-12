package com.academy.projects.ecommerce.approvalmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class OpenRequestExistsException extends RuntimeException {
    public OpenRequestExistsException(String id) {
        super("Open request already exists with id '" + id + "'!!!");
    }
}
