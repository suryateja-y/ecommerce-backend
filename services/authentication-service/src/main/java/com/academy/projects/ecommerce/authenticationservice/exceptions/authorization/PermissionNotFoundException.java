package com.academy.projects.ecommerce.authenticationservice.exceptions.authorization;

import lombok.ToString;

@ToString
public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(String permissionName) {
        super("Permission: '" + permissionName + "' not found!!!");
    }
}
