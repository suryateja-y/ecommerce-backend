package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.LoginRequestDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.SignUpRequestDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.SignUpResponseDto;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.UserPermissionsDto;

public interface IAuthenticationService {
    String login(LoginRequestDto requestDto);
    SignUpResponseDto signup(SignUpRequestDto requestDto);
    UserPermissionsDto validateAndGetRoles(String token);
}
