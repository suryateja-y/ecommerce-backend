package com.academy.projects.ecommerce.authenticationservice.exceptions.authorization;

import lombok.ToString;

@ToString
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleName) {
        super("Role: '" + roleName + "' not found!!!");
    }
}
