package com.academy.projects.ecommerce.apigateway.services;

import java.util.List;

public interface ITokenProvider {
    String generateToken(String email, List<String> rolesAndPermissions);
}