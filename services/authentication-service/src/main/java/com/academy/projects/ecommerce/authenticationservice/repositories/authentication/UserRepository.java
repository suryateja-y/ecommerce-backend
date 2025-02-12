package com.academy.projects.ecommerce.authenticationservice.repositories.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.User;
import com.academy.projects.ecommerce.authenticationservice.models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndUserType(String email, UserType userType);
}
