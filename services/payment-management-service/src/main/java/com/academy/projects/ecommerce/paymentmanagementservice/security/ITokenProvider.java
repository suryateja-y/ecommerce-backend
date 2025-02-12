package com.academy.projects.ecommerce.paymentmanagementservice.security;

import java.util.List;

public interface ITokenProvider {

    boolean isExpired(String token);

    String getUserId(String token);

    String getIssuer(String token);

    void validate(String token) throws InvalidTokenException;

    List<String> getPermissions(String token);
}