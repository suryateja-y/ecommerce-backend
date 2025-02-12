package com.academy.projects.ecommerce.authenticationservice.services.authorization;

import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import com.academy.projects.ecommerce.authenticationservice.models.Role;

import java.util.List;
import java.util.Set;

public interface IRoleService {
    Role createRole(Role role);
    Role addPermissionsToRole(String roleId, List<String> permissions);

    List<Role> getAllRoles(int iPageNumber, int iPageSize);
    Set<Permission> getPermissionsInRole(String roleId);
}
