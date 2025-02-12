package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.TokenRequest;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.TokenResponse;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.ValidationRequest;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.ValidationResponse;

public interface ITokenProvider {
    TokenResponse generateToken(TokenRequest tokenRequest);
    ValidationResponse validate(ValidationRequest validationRequest);
}