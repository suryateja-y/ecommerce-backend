package com.academy.projects.ecommerce.inventorymanagementservice.clients.configuration;

import com.academy.projects.ecommerce.inventorymanagementservice.security.TokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenManager.getToken());
    }
}
