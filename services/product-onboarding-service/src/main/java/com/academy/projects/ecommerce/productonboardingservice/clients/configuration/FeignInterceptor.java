package com.academy.projects.ecommerce.productonboardingservice.clients.configuration;

import com.academy.projects.ecommerce.productonboardingservice.security.TokenStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenStorage.getToken());
    }
}
