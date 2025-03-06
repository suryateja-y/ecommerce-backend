package com.academy.projects.trackingmanagementservice.security;

import java.util.List;

public interface ITokenService {
    String validate(String token);
    List<String> getPermissions(String token);
}
