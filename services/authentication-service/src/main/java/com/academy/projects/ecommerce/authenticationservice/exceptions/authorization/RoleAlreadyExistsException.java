package com.academy.projects.ecommerce.authenticationservice.exceptions.authorization;

import lombok.ToString;

@ToString
public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String roleName) {
        super("Role: " + roleName + " already exists!!!");
    }
}
