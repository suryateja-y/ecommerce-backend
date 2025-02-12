package com.academy.projects.ecommerce.authenticationservice.repositories.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByUserId(String name);
}
