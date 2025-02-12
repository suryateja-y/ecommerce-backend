package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthenticationResponseDto implements Serializable {
    private String email;
    private String token;
}
