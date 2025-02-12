package com.academy.projects.ecommerce.apigateway.services;

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

    @Override
    public String generateToken(String email, List<String> rolesAndPermissions) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expirationTime);
        return JWT
                .create()
                .withSubject(email)
                .withIssuer(issuer)
                .withIssuedAt(currentDate)
                .withExpiresAt(expirationDate)
                .withClaim("permissions", rolesAndPermissions)
                .sign(Algorithm.HMAC256(secret));
    }
}
