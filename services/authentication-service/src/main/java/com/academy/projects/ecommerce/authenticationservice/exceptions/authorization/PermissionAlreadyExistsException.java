package com.academy.projects.ecommerce.authenticationservice.exceptions.authorization;

import lombok.ToString;

@ToString
public class PermissionAlreadyExistsException extends RuntimeException {
    public PermissionAlreadyExistsException(String permissionName) {
        super("Permission: '" + permissionName + "' already exists!!!");
    }
}
