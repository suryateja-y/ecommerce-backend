package com.academy.projects.ecommerce.authenticationservice.services.authentication;

public interface ITokenService {
    String generateToken(String userId);
    String validate(String token);
}
