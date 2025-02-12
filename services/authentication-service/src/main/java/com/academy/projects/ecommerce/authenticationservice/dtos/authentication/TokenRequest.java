package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenRequest {
    private String userId;
    private String secret;
    private Long expirationTime;
    private String issuer;
}
