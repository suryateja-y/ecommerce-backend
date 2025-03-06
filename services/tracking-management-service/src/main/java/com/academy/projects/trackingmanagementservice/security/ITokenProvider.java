package com.academy.projects.trackingmanagementservice.security;

import java.util.List;

public interface ITokenProvider {
    ValidationResponse validate(ValidationRequest validationRequest);
    List<String> getPermissions(String token);
}