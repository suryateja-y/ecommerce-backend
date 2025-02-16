package com.academy.projects.ecommerce.apigateway.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;

@Configuration
public class RouteConfigurator {
    @Value("${application.version}")
    private String version;

    private final List<String> openEndPoints = List.of(
            "/api/" + version + "/authentication/signup",
            "/api/" + version + "/authentication/login",
            "/api/" + version + "/authentication/validate",
            "/api/" + version + "/authentication/logout",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> openEndPoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}
