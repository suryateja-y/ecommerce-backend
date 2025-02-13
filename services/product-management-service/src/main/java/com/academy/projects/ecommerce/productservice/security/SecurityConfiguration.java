package com.academy.projects.ecommerce.productservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    @Value("${application.version}")
    private String version;

    @Value("${application.security.allow.generic}")
    private List<String> genericAllowed;

    @Value("${application.security.allow.specific}")
    private List<String> specificAllowed;

    private final Authentication authenticate;

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDenierErrorHandler accessDenierErrorHandler;

    @Autowired
    public SecurityConfiguration(Authentication authenticate, AuthenticationEntryPoint authenticationEntryPoint, AccessDenierErrorHandler accessDenierErrorHandler) {
        this.authenticate = authenticate;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDenierErrorHandler = accessDenierErrorHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrls()).permitAll()
                        .anyRequest().authenticated())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .addFilterBefore(authenticate, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDenierErrorHandler))
                .logout(handler -> handler.logoutUrl("/api/" + version + "/authentication/logout")
                        .logoutSuccessUrl("/api/" + version + "/authentication/login")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    private String[] allowUrls() {
        List<String> allowUrls = new LinkedList<>();
        if((genericAllowed != null) && (!genericAllowed.isEmpty())) allowUrls.addAll(genericAllowed);
        if((specificAllowed != null) && (!specificAllowed.isEmpty())) allowUrls.addAll(specificAllowed);
        return allowUrls.toArray(new String[0]);
    }
}
