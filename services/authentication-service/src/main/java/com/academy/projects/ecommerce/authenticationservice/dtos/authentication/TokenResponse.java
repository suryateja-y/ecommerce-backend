package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class TokenResponse {
    private String userId;
    private String token;
    private Date expiresAt;
}
