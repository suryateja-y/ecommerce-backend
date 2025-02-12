package com.academy.projects.ecommerce.authenticationservice.dtos.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateRoleRequestDto implements Serializable {
    private String roleName;
    private String description;
}
