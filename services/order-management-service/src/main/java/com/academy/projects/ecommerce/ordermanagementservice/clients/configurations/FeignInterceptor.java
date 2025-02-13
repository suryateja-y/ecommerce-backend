package com.academy.projects.ecommerce.ordermanagementservice.clients.configurations;

import com.academy.projects.ecommerce.ordermanagementservice.security.TokenStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenStorage.getToken());
    }
}
