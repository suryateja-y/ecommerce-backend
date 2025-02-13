package com.academy.projects.ecommerce.notificationservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Authorization_Token_Service")
public class TokenService implements ITokenService {
    @Value("${application.gateway-secret}")
    private String secret;

    @Value("${application.gateway-issuer}")
    private String issuer;

    private final ITokenProvider tokenProvider;

    @Autowired
    public TokenService(final ITokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public String validate(String token) {
        ValidationResponse response = tokenProvider.validate(forToken(token));
        if(response.getResult().equals(ValidationResult.EXPIRED)) throw new InvalidTokenException("Token got expired!!! Please re-login!!!");
        if(response.getResult().equals(ValidationResult.INVALID)) throw new InvalidTokenException("Invalid Token!!!");
        return response.getUserId();
    }

    @Override
    public List<String> getPermissions(String token) {
        return tokenProvider.getPermissions(token);
    }

    private ValidationRequest forToken(String token) {
        return ValidationRequest.builder()
                .token(token)
                .secret(secret)
                .issuer(issuer)
                .build();
    }
}
