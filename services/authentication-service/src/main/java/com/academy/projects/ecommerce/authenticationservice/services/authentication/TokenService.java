package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.*;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.InvalidTokenException;
import com.academy.projects.ecommerce.authenticationservice.models.Token;
import com.academy.projects.ecommerce.authenticationservice.repositories.authentication.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService implements ITokenService {
    @Value("${application.secret}")
    private String secret;

    @Value("${application.expiration-time}")
    private Long expirationTime;

    @Value("${application.issuer}")
    private String issuer;

    private final ITokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(final ITokenProvider tokenProvider, final TokenRepository tokenRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
    }
    @Override
    public String generateToken(String userId) {
        Token token = tokenRepository.findByUserId(userId).orElse(null);
        if(!((token == null) || token.getExpiresAt().before(new Date()))) return token.getToken();
        if(token != null) tokenRepository.delete(token);
        TokenResponse tokenResponse = tokenProvider.generateToken(forUser(userId));
        tokenRepository.save(from(tokenResponse));
        return tokenResponse.getToken();
    }

    @Override
    public String validate(String token) {
        ValidationResponse response = tokenProvider.validate(forToken(token));
        if(response.getResult().equals(ValidationResult.EXPIRED)) throw new InvalidTokenException("Token got expired!!! Please re-login!!!");
        if(response.getResult().equals(ValidationResult.INVALID)) throw new InvalidTokenException("Invalid Token!!!");
        Token savedToken = tokenRepository.findByUserId(response.getUserId()).orElseThrow(() -> new InvalidTokenException("Invalid Token"));
        if(!savedToken.getUserId().equals(response.getUserId())) throw new InvalidTokenException("Invalid Token");
        if(savedToken.getExpiresAt().before(new Date())) throw new InvalidTokenException("Token got expired!!! Please re-login!!!");
        return savedToken.getUserId();
    }

    private TokenRequest forUser(String userId) {
        return TokenRequest.builder()
                .userId(userId)
                .secret(secret)
                .issuer(issuer)
                .expirationTime(expirationTime)
                .build();
    }

    private ValidationRequest forToken(String token) {
        return ValidationRequest.builder()
                .token(token)
                .secret(secret)
                .issuer(issuer)
                .build();
    }

    private Token from(TokenResponse tokenResponse) {
        Token token = new Token();
        token.setToken(tokenResponse.getToken());
        token.setUserId(tokenResponse.getUserId());
        token.setExpiresAt(tokenResponse.getExpiresAt());
        return token;
    }
}
