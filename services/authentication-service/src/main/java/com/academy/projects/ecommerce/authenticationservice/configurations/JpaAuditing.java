package com.academy.projects.ecommerce.authenticationservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditing {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            if(SecurityContextHolder.getContext().getAuthentication() != null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                try {
                    User user = (User) principal;
                    return Optional.of(user.getUsername());
                } catch(Exception exception) {
                    return Optional.of("NEW_USER");
                }
            } else {
                return Optional.of("SYSTEM");
            }
        };
    }
}
