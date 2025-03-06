package com.academy.projects.trackingmanagementservice.clients.configuration;

import com.academy.projects.trackingmanagementservice.security.TokenStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenStorage.getToken());
    }
}
