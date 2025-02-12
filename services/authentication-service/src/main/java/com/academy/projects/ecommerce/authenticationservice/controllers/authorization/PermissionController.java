package com.academy.projects.ecommerce.authenticationservice.controllers.authorization;

import com.academy.projects.ecommerce.authenticationservice.dtos.authorization.CreatePermissionRequestDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authorization.CreatePermissionResponseDto;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.PermissionAlreadyExistsException;
import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import com.academy.projects.ecommerce.authenticationservice.services.authorization.IPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/authorization/permissions")
public class PermissionController {
    private final IPermissionService permissionService;

    private final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('CRUD_PERMISSION')")
    public ResponseEntity<CreatePermissionResponseDto> createPermission(CreatePermissionRequestDto createPermissionRequestDto) {
        Permission permission = permissionService.createPermission(from(createPermissionRequestDto));
        CreatePermissionResponseDto responseDto = new CreatePermissionResponseDto();
        responseDto.setCreatedAt(permission.getCreatedAt());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('CRUD_PERMISSION')")
    public ResponseEntity<List<Permission>> getPermissions(@RequestParam(required = false) int page, @RequestParam(required = false) int pageSize) {
        return new ResponseEntity<>(permissionService.getPermissions(page, pageSize), HttpStatus.OK);
    }

    private Permission from(CreatePermissionRequestDto createPermissionRequestDto) {
        Permission permission = new Permission();
        permission.setPermissionName(createPermissionRequestDto.getPermissionName());
        permission.setDescription(createPermissionRequestDto.getDescription());
        return permission;
    }

    @ExceptionHandler(PermissionAlreadyExistsException.class)
    public ResponseEntity<String> handlePermissionAlreadyExistsException(PermissionAlreadyExistsException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

}
