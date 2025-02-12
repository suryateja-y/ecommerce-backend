package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTProvider implements ITokenProvider {

    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + tokenRequest.getExpirationTime());
        String tokenString = JWT
                .create()
                .withSubject(tokenRequest.getUserId())
                .withIssuer(tokenRequest.getIssuer())
                .withIssuedAt(currentDate)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(tokenRequest.getSecret()));
        return TokenResponse.builder()
                .userId(tokenRequest.getUserId())
                .token(tokenString)
                .expiresAt(expirationDate)
                .build();
    }

    private String getUserId(String token) {
        return JWT.decode(token).getSubject();
    }

    private String getIssuer(String token) {
        return JWT.decode(token).getIssuer();
    }

    @Override
    public ValidationResponse validate(ValidationRequest request) {
        ValidationResult result = ValidationResult.VALID;
        String userId = "";
        try {
            JWT.require(Algorithm.HMAC256(request.getSecret())).build().verify(request.getToken());
            if(!getIssuer(request.getToken()).equals(request.getIssuer())) throw new RuntimeException("Invalid token");
            userId = getUserId(request.getToken());
        } catch (Exception exception) {
            result = exception.getMessage().contains("expire") ? ValidationResult.EXPIRED : ValidationResult.INVALID;
        }
        return ValidationResponse.builder()
                .userId(userId)
                .result(result)
                .build();
    }
}
