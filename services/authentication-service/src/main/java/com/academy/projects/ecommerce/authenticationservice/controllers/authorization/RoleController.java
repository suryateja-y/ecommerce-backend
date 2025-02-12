package com.academy.projects.ecommerce.authenticationservice.controllers.authorization;

import com.academy.projects.ecommerce.authenticationservice.dtos.authorization.AddPermissionsResponseDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authorization.CreateRoleRequestDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authorization.CreateRoleResponseDto;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.PermissionNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleAlreadyExistsException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import com.academy.projects.ecommerce.authenticationservice.models.Role;
import com.academy.projects.ecommerce.authenticationservice.services.authorization.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/${application.version}/authorization/roles")
public class RoleController {
    private final IRoleService roleService;

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('CRUD_ROLE')")
    public ResponseEntity<CreateRoleResponseDto> createRole(@RequestBody CreateRoleRequestDto createRoleRequestDto) {
        Role role = roleService.createRole(from(createRoleRequestDto));
        CreateRoleResponseDto createRoleResponseDto = new CreateRoleResponseDto();
        createRoleResponseDto.setCreatedAt(role.getCreatedAt());
        return new ResponseEntity<>(createRoleResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('CRUD_ROLE')")
    public ResponseEntity<AddPermissionsResponseDto> addPermissions(@RequestBody List<String> permissions, @PathVariable String roleId) {
        Role role = roleService.addPermissionsToRole(roleId, permissions);
        return new ResponseEntity<>(from(role), HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('CRUD_ROLE')")
    public ResponseEntity<List<Role>> getRoles(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Role> roles = roleService.getAllRoles(page, pageSize);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<Set<Permission>> getPermissions(@PathVariable String roleId) {
        Set<Permission> permissions = roleService.getPermissionsInRole(roleId);
        return ResponseEntity.ok(permissions);
    }

    private AddPermissionsResponseDto from(Role role) {
        AddPermissionsResponseDto addPermissionsResponseDto = new AddPermissionsResponseDto();
        addPermissionsResponseDto.setModifiedAt(role.getModifiedAt());
        return addPermissionsResponseDto;
    }

    private Role from(CreateRoleRequestDto createRoleRequestDto) {
        Role role = new Role();
        role.setRoleName(createRoleRequestDto.getRoleName());
        role.setDescription(createRoleRequestDto.getDescription());
        return role;
    }

    @ExceptionHandler({PermissionNotFoundException.class, RoleNotFoundException.class, PermissionNotFoundException.class})
    public ResponseEntity<String> handler(RuntimeException exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<String> handler(RoleAlreadyExistsException exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
