package com.academy.projects.ecommerce.authenticationservice.repositories.authorization;

import com.academy.projects.ecommerce.authenticationservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);
}
