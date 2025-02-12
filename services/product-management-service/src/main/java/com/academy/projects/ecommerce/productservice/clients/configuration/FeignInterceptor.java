package com.academy.projects.ecommerce.productservice.clients.configuration;

import com.academy.projects.ecommerce.productservice.security.TokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenManager.getToken());
    }
}
