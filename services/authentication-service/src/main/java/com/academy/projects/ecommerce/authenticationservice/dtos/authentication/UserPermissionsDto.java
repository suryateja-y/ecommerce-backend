package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.UserState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPermissionsDto implements Serializable {
    private String message;
    private String userId;
    private List<String> rolesAndPermissions;
    private UserState userState;
}
