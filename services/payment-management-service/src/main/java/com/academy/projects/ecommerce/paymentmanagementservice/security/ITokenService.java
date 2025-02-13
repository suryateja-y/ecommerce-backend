package com.academy.projects.ecommerce.paymentmanagementservice.security;

import java.util.List;

public interface ITokenService {
    String validate(String token);
    List<String> getPermissions(String token);
}
