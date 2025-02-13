package com.academy.projects.ecommerce.productonboardingservice.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidationRequest {
    private String token;
    private String secret;
    private String issuer;
}
