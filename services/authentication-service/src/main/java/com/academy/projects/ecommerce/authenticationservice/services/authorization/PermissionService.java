package com.academy.projects.ecommerce.authenticationservice.services.authorization;

import com.academy.projects.ecommerce.authenticationservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.PermissionAlreadyExistsException;
import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService {
    private final PermissionRepository permissionRepository;
    private final IdGenerator idGenerator;

    private final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Autowired
    public PermissionService(final PermissionRepository permissionRepository, final IdGenerator idGenerator) {
        this.permissionRepository = permissionRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Permission createPermission(Permission permission) {
        Optional<Permission> permissionOptional = permissionRepository.findByPermissionName(permission.getPermissionName());
        if(permissionOptional.isPresent()) throw new PermissionAlreadyExistsException(permission.getPermissionName());
        Permission createdPermission = new Permission();
        createdPermission.setPermissionName(permission.getPermissionName());
        createdPermission.setDescription(permission.getDescription());
        createdPermission.setId(idGenerator.getId(Permission.SEQUENCE_NAME));
        logger.info("Permission: '{}' created!!!", permission.getPermissionName());
        return permissionRepository.save(createdPermission);
    }

    @Override
    public List<Permission> getPermissions(int iPageNumber, int iPageSize) {
        iPageNumber = iPageNumber == 1 ? 0 : iPageNumber;
        iPageSize = iPageSize == 1 ? 5 : iPageSize;
        Pageable pageable = PageRequest.of(iPageNumber, iPageSize);
        Page<Permission> page = permissionRepository.findAll(pageable);
        return page.getContent();
    }
}
