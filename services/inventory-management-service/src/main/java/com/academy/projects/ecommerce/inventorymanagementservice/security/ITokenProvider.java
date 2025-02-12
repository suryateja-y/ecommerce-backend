package com.academy.projects.ecommerce.inventorymanagementservice.security;

import java.util.List;

public interface ITokenProvider {

    boolean isExpired(String token);

    String getEmail(String token);

    String getIssuer(String token);

    void validate(String token) throws InvalidTokenException;

    List<String> getPermissions(String token);
}