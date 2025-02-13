package com.academy.projects.ecommerce.productservice.security;

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
import java.util.List;

@Service
public class Authentication extends OncePerRequestFilter {

    private final ITokenService tokenService;

    @Value("${application.security.gateway-prefix}")
    private String gatewayPrefix;

    @Autowired
    public Authentication(final ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            if(token != null) {
                String userId = tokenService.validate(token);
                TokenStorage.setToken(token);
                List<String> permissionList = tokenService.getPermissions(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, permissionList.stream().map(SimpleGrantedAuthority::new).toList());
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
     * Method to get the Token from the request header "Authorization"
     * @param request Http Request
     * @return Token string
     */
    private String getToken(final HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if ((token != null) && (token.startsWith(gatewayPrefix))) {
            return token.substring(gatewayPrefix.length()).trim();
        }
        return null;
    }
}
