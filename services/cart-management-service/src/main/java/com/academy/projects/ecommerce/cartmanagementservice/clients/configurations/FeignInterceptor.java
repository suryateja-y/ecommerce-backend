package com.academy.projects.ecommerce.cartmanagementservice.clients.configurations;

import com.academy.projects.ecommerce.cartmanagementservice.security.TokenStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenStorage.getToken());
    }
}
