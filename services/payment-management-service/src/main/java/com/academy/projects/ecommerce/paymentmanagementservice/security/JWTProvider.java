package com.academy.projects.ecommerce.paymentmanagementservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("JavaWebToken_Provider")
public class JWTProvider implements ITokenProvider {
    private String getUserId(String token) {
        return JWT.decode(token).getSubject();
    }
    private String getIssuer(String token) {
        return JWT.decode(token).getIssuer();
    }

    @SuppressWarnings("DuplicatedCode")
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

    @Override
    public List<String> getPermissions(String token) {
        return JWT.decode(token).getClaim("permissions").asList(String.class);
    }
}
