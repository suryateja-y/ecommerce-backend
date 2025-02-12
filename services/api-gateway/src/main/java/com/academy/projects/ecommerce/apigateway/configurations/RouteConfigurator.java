package com.academy.projects.ecommerce.apigateway.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;

@Configuration
public class RouteConfigurator {
    private static final List<String> openEndPoints = List.of(
            "/api/v1/authentication/signup",
            "/api/v1/authentication/login",
            "/api/v1/authentication/validate",
            "/api/v1/authentication/logout",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> openEndPoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
