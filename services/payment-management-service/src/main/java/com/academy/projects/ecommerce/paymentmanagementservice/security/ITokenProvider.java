package com.academy.projects.ecommerce.paymentmanagementservice.security;

import java.util.List;

public interface ITokenProvider {
    ValidationResponse validate(ValidationRequest validationRequest);
    List<String> getPermissions(String token);
}