package com.academy.projects.ecommerce.ordermanagementservice.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class Authenticate extends OncePerRequestFilter {

    private final ITokenProvider tokenProvider;

    @Value("${application.issuer}")
    private String issuer;

    @Autowired
    public Authenticate(final ITokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if(token != null) {
                String email = validateToken(token);
                TokenManager.setToken(token);
                List<String> permissionList = tokenProvider.getPermissions(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, permissionList.stream().map(SimpleGrantedAuthority::new).toList());
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
        tokenProvider.validate(token);
        if(tokenProvider.isExpired(token)) throw new TokenExpiredException("Token expired!!! Please re-login!!!", new Date().toInstant());
        if(!tokenProvider.getIssuer(token).equals(issuer)) throw new InvalidTokenException("Invalid Issuer!!!");

        String userId = tokenProvider.getUserId(token);
        if(userId == null) throw new InvalidTokenException("Token does not have required User details!!!");
        return userId;
    }

    /**
     * Method to get the Token from the request header "Authorization"
     * @param request Http Request
     * @return Token string
     */
    private String getToken(final HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if ((token != null) && (token.startsWith("Gateway"))) {
            return token.substring(8);
        }
        return null;
    }
}
