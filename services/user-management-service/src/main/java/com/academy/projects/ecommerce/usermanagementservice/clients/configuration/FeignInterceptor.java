package com.academy.projects.ecommerce.usermanagementservice.clients.configuration;

import com.academy.projects.ecommerce.usermanagementservice.security.TokenStorage;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String url = requestTemplate.url();
        if(url.contains("/authentication/register") || url.contains("/approvals/register")) return;
        requestTemplate
                .header("Authorization", "Gateway " + TokenStorage.getToken());
    }
}
