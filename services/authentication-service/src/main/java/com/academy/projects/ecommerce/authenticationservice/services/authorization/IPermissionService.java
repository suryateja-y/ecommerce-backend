package com.academy.projects.ecommerce.authenticationservice.services.authorization;

import com.academy.projects.ecommerce.authenticationservice.models.Permission;

import java.util.List;

public interface IPermissionService {
    Permission createPermission(Permission permission);
    List<Permission> getPermissions(int iPageNumber, int iPageSize);
}
