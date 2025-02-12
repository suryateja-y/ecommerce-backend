package com.academy.projects.ecommerce.authenticationservice.services.authorization;

import com.academy.projects.ecommerce.authenticationservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.PermissionNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleAlreadyExistsException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import com.academy.projects.ecommerce.authenticationservice.models.Role;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.PermissionRepository;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final IdGenerator idGenerator;

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, IdGenerator idGenerator) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Role createRole(Role role) {
        Optional<Role> roleOptional = roleRepository.findByRoleName(role.getRoleName());
        if (roleOptional.isPresent()) throw new RoleAlreadyExistsException(role.getRoleName());
        role.setId(idGenerator.getId(Role.SEQUENCE_NAME));
        logger.info("Role: '{}' created!!!", role.getRoleName());
        return roleRepository.save(role);
    }

    @Override
    public Role addPermissionsToRole(String roleId, List<String> permissions) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
        List<Permission> permissionList = checkPermissionsExist(permissions);
        role.getPermissions().addAll(permissionList);
        logger.info("Permissions: {} added into the Role: '{}'", permissions, role.getRoleName());
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles(int iPageNumber, int iPageSize) {
        Pageable pageable = PageRequest.of(iPageNumber, iPageSize);
        Page<Role> page =  roleRepository.findAll(pageable);
        return page.getContent();
    }

    @Override
    public Set<Permission> getPermissionsInRole(String roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
        return role.getPermissions();
    }

    private List<Permission> checkPermissionsExist(List<String> permissions) {
        List<Permission> permissionList = new ArrayList<>();
        for(String permission : permissions) {
            Permission savedPermission = permissionRepository.findByPermissionName(permission).orElseThrow(() -> new PermissionNotFoundException(permission));
            permissionList.add(savedPermission);
        }
        return permissionList;
    }
}
