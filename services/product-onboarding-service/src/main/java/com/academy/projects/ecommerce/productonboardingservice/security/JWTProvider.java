package com.academy.projects.ecommerce.productonboardingservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JWTProvider implements ITokenProvider {

    @Value(("${application.secret}"))
    private String secret;

    @Value("${application.expiration-time}")
    private long expirationTime;

    @Value("${application.issuer}")
    private String issuer;

    public String getEmail(String token) {
        return JWT.decode(token).getSubject();
    }

    @Override
    public String getIssuer(String token) {
        return JWT.decode(token).getIssuer();
    }

    @Override
    public void validate(String token) throws InvalidTokenException {
        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        } catch (Exception exception) {
            throw new InvalidTokenException(exception.getMessage());
        }
    }

    @Override
    public List<String> getPermissions(String token) {
        return JWT.decode(token).getClaim("permissions").asList(String.class);
    }

    public boolean isExpired(String token) {
        return JWT.decode(token).getExpiresAt().before(new Date());
    }
}
