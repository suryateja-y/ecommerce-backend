package com.academy.projects.ecommerce.cartmanagementservice.security;

import java.util.List;

public interface ITokenService {
    String validate(String token);
    List<String> getPermissions(String token);
}
