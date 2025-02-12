package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequestDto implements Serializable {
    private String email;
    private String password;
    private UserType userType;
}
