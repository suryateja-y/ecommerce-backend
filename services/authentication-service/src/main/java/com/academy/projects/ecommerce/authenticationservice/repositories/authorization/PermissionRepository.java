package com.academy.projects.ecommerce.authenticationservice.repositories.authorization;

import com.academy.projects.ecommerce.authenticationservice.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
