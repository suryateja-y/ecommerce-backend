package com.academy.projects.ecommerce.productservice.clients.configuration;

import com.academy.projects.ecommerce.productservice.security.TokenStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate
                .header("Authorization", "Gateway " + TokenStorage.getToken());
    }
}
