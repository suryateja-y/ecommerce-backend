package com.academy.projects.ecommerce.usermanagementservice.repositories;

import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailAndUserType(String email, UserType userType);
}
