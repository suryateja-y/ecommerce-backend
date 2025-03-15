package com.academy.projects.ecommerce.authenticationservice.repositories.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.User;
import com.academy.projects.ecommerce.authenticationservice.models.UserType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Cacheable(value = "users", key = "#userId")
    @NonNull
    Optional<User> findById(@NonNull String userId);
    Optional<User> findByEmailAndUserType(String email, UserType userType);
}
