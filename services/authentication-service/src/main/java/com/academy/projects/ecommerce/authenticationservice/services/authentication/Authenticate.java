package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.ValidationRequest;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.ValidationResponse;
import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.ValidationResult;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class Authenticate extends OncePerRequestFilter {

    private final ITokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Value("${application.gateway-secret}")
    private String secret;

    @Value("${application.gateway-issuer}")
    private String issuer;

    @Autowired
    public Authenticate(final ITokenProvider tokenProvider, final UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if(token != null) {
                String userId = validateToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | InvalidTokenException exception) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(exception.getMessage());
        }

    }

    /**
     * Method to validate the token and details
     * @param token Token string
     * @return Email if the token is valid
     */
    private String validateToken(String token) {
        ValidationResponse response = tokenProvider.validate(forToken(token));
        if(response.getResult().equals(ValidationResult.INVALID)) throw new InvalidTokenException("Invalid Token!!!");
        if(response.getResult().equals(ValidationResult.EXPIRED)) throw new InvalidTokenException("Token expired!!! Please re-login!!!");
        return response.getUserId();
    }

    /**
     * Method to get the Token from the request header "Authorization"
     * @param request Http Request
     * @return Token string
     */
    private String getToken(final HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if ((token != null) && (token.startsWith("Gateway "))) {
            return token.substring(7).trim();
        }
        return null;
    }

    private ValidationRequest forToken(String token) {
        return ValidationRequest.builder()
                .token(token)
                .secret(secret)
                .issuer(issuer)
                .build();
    }
}
